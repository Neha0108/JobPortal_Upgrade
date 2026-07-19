import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ExperienceService } from '../../services/experience/experience-service';
import { ExperienceResponse } from '../../models/candidateprofile';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-experience',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './experience.html',
  styleUrl: './experience.css',
})
export class Experience implements OnInit{

  private readonly fb = inject(FormBuilder);
  private readonly experienceService = inject(ExperienceService);

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly entries = signal<ExperienceResponse[]>([]);

  readonly editingId = signal<string | null>(null);
  readonly isSaving = signal(false);
  readonly saveError = signal<string | null>(null);
  readonly pendingDeleteId = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    companyName: ['', [Validators.required]],
    jobTitle: ['', [Validators.required]],
    description: [''],
    startDate: [''],
    endDate: [''],
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.experienceService.getMyExperience().subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.entries.set(res.data ?? []);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load your experience. Please try again.');
      },
    });
  }

  startEdit(entry: ExperienceResponse): void {
    this.editingId.set(entry.id);
    this.saveError.set(null);
    this.form.setValue({
      companyName: entry.companyName,
      jobTitle: entry.jobTitle,
      description: entry.description ?? '',
      startDate: entry.startDate ?? '',
      endDate: entry.endDate ?? '',
    });
  }

  cancelEdit(): void {
    this.editingId.set(null);
    this.saveError.set(null);
    this.form.reset({ companyName: '', jobTitle: '', description: '', startDate: '', endDate: '' });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    this.saveError.set(null);

    const raw = this.form.getRawValue();
    const request = {
      companyName: raw.companyName,
      jobTitle: raw.jobTitle,
      description: raw.description || undefined,
      startDate: raw.startDate || undefined,
      endDate: raw.endDate || undefined,
    };

    const editingId = this.editingId();
    const request$ = editingId
      ? this.experienceService.update(editingId, request)
      : this.experienceService.add(request);

    request$.subscribe({
      next: (res) => {
        this.isSaving.set(false);
        if (!res.data) return;
        if (editingId) {
          this.entries.update((list) => list.map((e) => (e.id === editingId ? res.data! : e)));
        } else {
          this.entries.update((list) => [res.data!, ...list]);
        }
        this.cancelEdit();
      },
      error: (err) => {
        this.isSaving.set(false);
        this.saveError.set(err?.error?.message ?? 'Could not save this entry. Please try again.');
      },
    });
  }

  delete(experienceId: string): void {
    if (!confirm('Remove this experience entry?')) return;

    this.pendingDeleteId.set(experienceId);
    this.experienceService.delete(experienceId).subscribe({
      next: () => {
        this.pendingDeleteId.set(null);
        this.entries.update((list) => list.filter((e) => e.id !== experienceId));
      },
      error: (err) => {
        this.pendingDeleteId.set(null);
        this.saveError.set(err?.error?.message ?? 'Could not remove this entry.');
      },
    });
  }
}
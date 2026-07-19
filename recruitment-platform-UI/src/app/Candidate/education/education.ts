import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { EducationService } from '../../services/education/education-service';
import { EducationResponse } from '../../models/candidateprofile';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-education',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './education.html',
  styleUrl: './education.css',
})
export class Education implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly educationService = inject(EducationService);

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly entries = signal<EducationResponse[]>([]);

  readonly editingId = signal<string | null>(null);
  readonly isSaving = signal(false);
  readonly saveError = signal<string | null>(null);
  readonly pendingDeleteId = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    institution: ['', [Validators.required]],
    degree: ['', [Validators.required]],
    fieldOfStudy: [''],
    startDate: [''],
    endDate: [''],
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.educationService.getMyEducation().subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.entries.set(res.data ?? []);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load your education. Please try again.');
      },
    });
  }

  startEdit(entry: EducationResponse): void {
    this.editingId.set(entry.id);
    this.saveError.set(null);
    this.form.setValue({
      institution: entry.institution,
      degree: entry.degree,
      fieldOfStudy: entry.fieldOfStudy ?? '',
      startDate: entry.startDate ?? '',
      endDate: entry.endDate ?? '',
    });
  }

  cancelEdit(): void {
    this.editingId.set(null);
    this.saveError.set(null);
    this.form.reset({ institution: '', degree: '', fieldOfStudy: '', startDate: '', endDate: '' });
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
      institution: raw.institution,
      degree: raw.degree,
      fieldOfStudy: raw.fieldOfStudy || undefined,
      startDate: raw.startDate || undefined,
      endDate: raw.endDate || undefined,
    };

    const editingId = this.editingId();
    const request$ = editingId
      ? this.educationService.update(editingId, request)
      : this.educationService.add(request);

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

  delete(educationId: string): void {
    if (!confirm('Remove this education entry?')) return;

    this.pendingDeleteId.set(educationId);
    this.educationService.delete(educationId).subscribe({
      next: () => {
        this.pendingDeleteId.set(null);
        this.entries.update((list) => list.filter((e) => e.id !== educationId));
      },
      error: (err) => {
        this.pendingDeleteId.set(null);
        this.saveError.set(err?.error?.message ?? 'Could not remove this entry.');
      },
    });
  }
}
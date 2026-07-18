import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CandidateProfileResponse } from '../../models/candidate';
import { CandidateService } from '../../services/candidate/candidate-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-candidateprofile',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './candidateprofile.html',
  styleUrl: './candidateprofile.css',
})
export class Candidateprofile implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly profileService = inject(CandidateService);

  readonly isLoading = signal(true);
  readonly isSaving = signal(false);
  readonly loadError = signal<string | null>(null);
  readonly saveError = signal<string | null>(null);
  readonly saveSuccess = signal(false);
  readonly profile = signal<CandidateProfileResponse | null>(null);

  readonly form = this.fb.nonNullable.group({
    fullName: ['', [Validators.required, Validators.maxLength(150)]],
    phone: ['', [Validators.maxLength(20)]],
    headline: ['', [Validators.maxLength(200)]],
    summary: [''],
  });

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.isLoading.set(true);
    this.loadError.set(null);

    this.profileService.getMyProfile().subscribe({
      next: (res) => {
        this.isLoading.set(false);
        const data = res.data;
        if (!data) return;
        this.profile.set(data);
        this.form.patchValue({
          fullName: data.fullName,
          phone: data.phone ?? '',
          headline: data.headline ?? '',
          summary: data.summary ?? '',
        });
      },
      error: (err) => {
        this.isLoading.set(false);
        this.loadError.set(err?.error?.message ?? 'Could not load your profile. Please try again.');
      },
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSaving.set(true);
    this.saveError.set(null);
    this.saveSuccess.set(false);

    const raw = this.form.getRawValue();
    this.profileService
      .updateMyProfile({
        fullName: raw.fullName,
        phone: raw.phone || undefined,
        headline: raw.headline || undefined,
        summary: raw.summary || undefined,
      })
      .subscribe({
        next: (res) => {
          this.isSaving.set(false);
          this.saveSuccess.set(true);
          if (res.data) this.profile.set(res.data);
        },
        error: (err) => {
          this.isSaving.set(false);
          this.saveError.set(err?.error?.message ?? 'Could not save your profile. Please try again.');
        },
      });
  }
}
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Recruiterjobs } from '../../services/job/recruiterjobs';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { JobType } from '../../models/job';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-jobform',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './jobform.html',
  styleUrl: './jobform.css',
})
export class Jobform  implements OnInit{
  
  private readonly fb = inject(FormBuilder);
  private readonly jobService = inject(Recruiterjobs);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
 
  private readonly jobId = this.route.snapshot.paramMap.get('jobId');
  readonly isEditMode = this.jobId !== null;
 
  readonly JobType = JobType;
  readonly isLoading = signal(this.isEditMode);
  readonly isSaving = signal(false);
  readonly loadError = signal<string | null>(null);
  readonly saveError = signal<string | null>(null);
 
  readonly form = this.fb.nonNullable.group({
    title: ['', [Validators.required]],
    description: ['', [Validators.required]],
    requirements: [''],
    location: [''],
    jobType: [JobType.FULL_TIME, [Validators.required]],
    minSalary: [null as number | null],
    maxSalary: [null as number | null],
  });
 
  ngOnInit(): void {
    if (this.isEditMode && this.jobId) {
      this.jobService.getMyJob(this.jobId).subscribe({
        next: (res) => {
          this.isLoading.set(false);
          const data = res.data;
          if (!data) return;
          this.form.patchValue({
            title: data.title,
            description: data.description,
            requirements: data.requirements ?? '',
            location: data.location ?? '',
            jobType: data.jobType,
            minSalary: data.minSalary,
            maxSalary: data.maxSalary,
          });
        },
        error: (err) => {
          this.isLoading.set(false);
          this.loadError.set(err?.error?.message ?? 'Could not load this job. Please try again.');
        },
      });
    }
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
      title: raw.title,
      description: raw.description,
      requirements: raw.requirements || undefined,
      location: raw.location || undefined,
      jobType: raw.jobType,
      minSalary: raw.minSalary ?? undefined,
      maxSalary: raw.maxSalary ?? undefined,
    };
 
    const request$ =
      this.isEditMode && this.jobId
        ? this.jobService.updateJob(this.jobId, request)
        : this.jobService.createJob(request);
 
    request$.subscribe({
      next: () => {
        this.isSaving.set(false);
        this.router.navigateByUrl('/recruiter/jobs');
      },
      error: (err) => {
        this.isSaving.set(false);
        this.saveError.set(err?.error?.message ?? 'Could not save this job. Please try again.');
      },
    });
  }
}

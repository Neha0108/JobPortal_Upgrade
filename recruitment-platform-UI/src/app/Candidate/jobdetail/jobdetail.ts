import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApplicationService } from '../../services/application/application-service';
import { JobService } from '../../services/job/job-service';
import { PublicJobResponse } from '../../models/job';
import { EnumLabelPipe } from '../../models/enum-label-pipe';
import { CommonModule } from '@angular/common';
import { PublicJobService } from '../../services/job/public-job-service';

@Component({
  selector: 'app-jobdetail',
  imports: [RouterLink,CommonModule, EnumLabelPipe],
  templateUrl: './jobdetail.html',
  styleUrl: './jobdetail.css',
})
export class Jobdetail implements OnInit{

  private readonly route = inject(ActivatedRoute);
  private readonly publicJobService = inject(PublicJobService);
  private readonly applicationService = inject(ApplicationService);
  private readonly savedJobService = inject(JobService);

  private readonly jobId = this.route.snapshot.paramMap.get('jobId')!;

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly job = signal<PublicJobResponse | null>(null);

  readonly isApplying = signal(false);
  readonly applyError = signal<string | null>(null);
  readonly applied = signal(false);

  readonly isSaving = signal(false);
  readonly saveError = signal<string | null>(null);
  readonly saved = signal(false);

  ngOnInit(): void {
    this.loadJob();
  }

  loadJob(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.publicJobService.getById(this.jobId).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.job.set(res.data ?? null);
      },
      error: (err: any) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load this job. It may no longer be available.');
      },
    });
  }

  apply(): void {
    this.isApplying.set(true);
    this.applyError.set(null);

    this.applicationService.apply(this.jobId).subscribe({
      next: () => {
        this.isApplying.set(false);
        this.applied.set(true);
      },
      error: (err: any) => {
        this.isApplying.set(false);
        this.applyError.set(err?.error?.message ?? 'Could not submit your application. Please try again.');
      },
    });
  }

  toggleSave(): void {
    this.isSaving.set(true);
    this.saveError.set(null);

    if (this.saved()) {
      this.savedJobService.unsave(this.jobId).subscribe({
        next: () => {
          this.isSaving.set(false);
          this.saved.set(!this.saved());
        },
        error: (err: any) => {
          this.isSaving.set(false);
          this.saveError.set(err?.error?.message ?? 'Could not update your saved jobs. Please try again.');
        },
      });
    } else {
      this.savedJobService.save(this.jobId).subscribe({
        next: () => {
          this.isSaving.set(false);
          this.saved.set(!this.saved());
        },
        error: (err: any) => {
          this.isSaving.set(false);
          this.saveError.set(err?.error?.message ?? 'Could not update your saved jobs. Please try again.');
        },
      });
    }
  }
}
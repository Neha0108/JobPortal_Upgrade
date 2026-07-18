import { Component, inject, signal } from '@angular/core';
import { JobResponse, JobStatus } from '../../models/job';
import { Recruiterjobs } from '../../services/job/recruiterjobs';
import { PageResponse } from '../../models/api-response';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EnumLabelPipe } from '../../models/enum-label-pipe';

@Component({
  selector: 'app-recruiter-job',
  imports: [CommonModule, RouterLink, EnumLabelPipe],
  templateUrl: './recruiter-job.html',
  styleUrl: './recruiter-job.css',
})
export class RecruiterJob {
  private readonly jobService = inject(Recruiterjobs);
 
  readonly JobStatus = JobStatus;
  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly result = signal<PageResponse<JobResponse> | null>(null);
  readonly currentPage = signal(0);
  readonly actioningId = signal<string | null>(null);
  readonly actionError = signal<string | null>(null);
 
  ngOnInit(): void {
    this.load();
  }
 
  load(page = 0): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.currentPage.set(page);
 
    this.jobService.getMyJobs(page, 20).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.result.set(res.data ?? null);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load your jobs. Please try again.');
      },
    });
  }
 
  changeStatus(jobId: string, status: JobStatus): void {
    this.actioningId.set(jobId);
    this.actionError.set(null);
 
    this.jobService.updateJobStatus(jobId, status).subscribe({
      next: () => {
        this.actioningId.set(null);
        this.load(this.currentPage());
      },
      error: (err) => {
        this.actioningId.set(null);
        this.actionError.set(err?.error?.message ?? 'Could not update job status.');
      },
    });
  }
 
  deleteJob(jobId: string): void {
    if (!confirm('Delete this job posting? This cannot be undone.')) return;
 
    this.actioningId.set(jobId);
    this.actionError.set(null);
 
    this.jobService.deleteJob(jobId).subscribe({
      next: () => {
        this.actioningId.set(null);
        this.load(this.currentPage());
      },
      error: (err) => {
        this.actioningId.set(null);
        this.actionError.set(err?.error?.message ?? 'Could not delete this job.');
      },
    });
  }
 
  nextPage(): void {
    const r = this.result();
    if (r && !r.last) this.load(this.currentPage() + 1);
  }
 
  prevPage(): void {
    if (this.currentPage() > 0) this.load(this.currentPage() - 1);
  }
}

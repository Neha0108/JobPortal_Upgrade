import { Component, inject, signal } from '@angular/core';
import { JobService } from '../../services/job/job-service';
import { PageResponse } from '../../models/api-response';
import { SavedJobResponse } from '../../models/job';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-savedjobs',
  imports: [CommonModule, RouterLink],
  templateUrl: './savedjobs.html',
  styleUrl: './savedjobs.css',
})
export class Savedjobs {

  private readonly savedJobService = inject(JobService);

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly result = signal<PageResponse<SavedJobResponse> | null>(null);
  readonly currentPage = signal(0);
  readonly removingId = signal<string | null>(null);

  ngOnInit(): void {
    this.load();
  }

  load(page = 0): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.currentPage.set(page);

    this.savedJobService.getMySavedJobs(page, 20).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.result.set(res.data ?? null);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load your saved jobs. Please try again.');
      },
    });
  }

  remove(jobId: string): void {
    this.removingId.set(jobId);
    this.savedJobService.unsave(jobId).subscribe({
      next: () => {
        this.removingId.set(null);
        this.load(this.currentPage());
      },
      error: () => {
        this.removingId.set(null);
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
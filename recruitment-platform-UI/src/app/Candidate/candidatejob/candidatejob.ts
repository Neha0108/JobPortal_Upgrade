import { Component, inject, OnInit, signal } from '@angular/core';
import { JobType, PublicJobResponse } from '../../models/job';
import { PageResponse } from '../../models/api-response';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { EnumLabelPipe } from '../../models/enum-label-pipe';
import { PublicJobService } from '../../services/job/public-job-service';

@Component({
  selector: 'app-candidatejob',
  imports: [ReactiveFormsModule, RouterLink, EnumLabelPipe],
  templateUrl: './candidatejob.html',
  styleUrl: './candidatejob.css',
})
export class Candidatejob implements OnInit{

  private readonly fb = inject(FormBuilder);
  private readonly publicJobService = inject(PublicJobService);

  readonly JobType = JobType;
  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly result = signal<PageResponse<PublicJobResponse> | null>(null);
  readonly currentPage = signal(0);

  readonly filters = this.fb.nonNullable.group({
    keyword: [''],
    location: [''],
    jobType: [''],
  });

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(page = 0): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.currentPage.set(page);

    const raw = this.filters.getRawValue();
    this.publicJobService
      .search({
        keyword: raw.keyword || undefined,
        location: raw.location || undefined,
        jobType: (raw.jobType as JobType) || undefined,
        page,
        size: 10,
      })
      .subscribe({
        next: (res) => {
          this.isLoading.set(false);
          this.result.set(res.data ?? null);
          console.log(res.data);
        },
        error: (err) => {
          this.isLoading.set(false);
          this.error.set(err?.error?.message ?? 'Could not load jobs. Please try again.');
        },
      });
  }

  applyFilters(): void {
    this.loadJobs(0);
  }

  nextPage(): void {
    const r = this.result();
    if (r && !r.last) this.loadJobs(this.currentPage() + 1);
  }

  prevPage(): void {
    if (this.currentPage() > 0) this.loadJobs(this.currentPage() - 1);
  }
}
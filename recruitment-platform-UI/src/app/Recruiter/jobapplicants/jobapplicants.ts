import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApplicantResponse, ApplicationStatus } from '../../models/job';
import { PageResponse } from '../../models/api-response';
import { Recruiterapplications } from '../../services/application/recruiterapplications';
import { EnumLabelPipe } from '../../models/enum-label-pipe';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-jobapplicants',
  imports: [RouterLink, EnumLabelPipe, CommonModule],
  templateUrl: './jobapplicants.html',
  styleUrl: './jobapplicants.css',
})
export class Jobapplicants implements OnInit{
  
  private readonly route = inject(ActivatedRoute);
  private readonly applicationService = inject(Recruiterapplications);
 
  private readonly jobId = this.route.snapshot.paramMap.get('jobId')!;
 
  readonly ApplicationStatus = ApplicationStatus;
  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly result = signal<PageResponse<ApplicantResponse> | null>(null);
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
 
    this.applicationService.getApplicantsForJob(this.jobId, page, 20).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.result.set(res.data ?? null);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load applicants. Please try again.');
      },
    });
  }
 
  shortlist(applicationId: string): void {
    this.actioningId.set(applicationId);
    this.actionError.set(null);
 
    this.applicationService.shortlist(applicationId).subscribe({
      next: () => {
        this.actioningId.set(null);
        this.load(this.currentPage());
      },
      error: (err) => {
        this.actioningId.set(null);
        this.actionError.set(err?.error?.message ?? 'Could not shortlist this candidate.');
      },
    });
  }
 
  reject(applicationId: string): void {
    this.actioningId.set(applicationId);
    this.actionError.set(null);
 
    this.applicationService.reject(applicationId).subscribe({
      next: () => {
        this.actioningId.set(null);
        this.load(this.currentPage());
      },
      error: (err) => {
        this.actioningId.set(null);
        this.actionError.set(err?.error?.message ?? 'Could not reject this candidate.');
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

import { Component, inject, signal } from '@angular/core';
import { ApplicationService } from '../../services/application/application-service';
import { PageResponse } from '../../models/api-response';
import { CandidateApplicationResponse } from '../../models/job';
import { CommonModule } from '@angular/common';
import { EnumLabelPipe } from '../../models/enum-label-pipe';

@Component({
  selector: 'app-applications',
  imports: [CommonModule, EnumLabelPipe],
  templateUrl: './applications.html',
  styleUrl: './applications.css',
})
export class Applications {
  
  private readonly applicationService = inject(ApplicationService);

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly result = signal<PageResponse<CandidateApplicationResponse> | null>(null);
  readonly currentPage = signal(0);

  ngOnInit(): void {
    this.load();
  }

  load(page = 0): void {
    this.isLoading.set(true);
    this.error.set(null);
    this.currentPage.set(page);

    this.applicationService.getMyApplications(page, 20).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.result.set(res.data ?? null);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load your applications. Please try again.');
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
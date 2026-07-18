import { Component, inject, OnInit, signal } from '@angular/core';
import { CandidateService } from '../../services/candidate/candidate-service';
import { CandidateDashboardResponse } from '../../models/candidate';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-canidatedashboard',
  imports: [ CommonModule],
  templateUrl: './canidatedashboard.html',
  styleUrl: './canidatedashboard.css',
})
export class Canidatedashboard implements OnInit {

  private readonly dashboardService = inject(CandidateService);

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly stats = signal<CandidateDashboardResponse | null>(null);

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.dashboardService.getMyDashboard().subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.stats.set(res.data ?? null);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load your dashboard. Please try again.');
      },
    });
  }
}
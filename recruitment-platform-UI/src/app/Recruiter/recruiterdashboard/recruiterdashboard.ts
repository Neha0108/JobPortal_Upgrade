import { Component, inject, OnInit, signal } from '@angular/core';
import { RecruiterService } from '../../services/recruiter/recruiter';
import { RecruiterDashboardResponse } from '../../models/recruiter';

@Component({
  selector: 'app-recruiterdashboard',
  imports: [],
  templateUrl: './recruiterdashboard.html',
  styleUrl: './recruiterdashboard.css',
})
export class Recruiterdashboard implements OnInit {

  private readonly dashboardService = inject(RecruiterService);

  readonly isLoading = signal(true);
  readonly error = signal<string | null>(null);
  readonly stats = signal<RecruiterDashboardResponse | null>(null);

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
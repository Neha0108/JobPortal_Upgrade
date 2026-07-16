import { Component, inject } from '@angular/core';
import { JobDTO } from '../../interface/job-request';
import { Recruiter } from '../../service/recruiter';
import { Jobform } from "../jobform/jobform";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [Jobform, CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  
  private jobService = inject(Recruiter);

  jobs: JobDTO[] = [];
  loading = false;
  error = '';

  showForm = false;
  editingJob: JobDTO | null = null;
  confirmDeleteId: number | null = null;

  get activeCount()   { return this.jobs.filter(j => j.active).length; }
  get inactiveCount() { return this.jobs.filter(j => !j.active).length; }

  ngOnInit(): void { this.loadJobs(); }

  loadJobs(): void {
    this.loading = true;
    this.error = '';
    this.jobService.getMyJobs().subscribe({
      next: (jobs) => { this.jobs = jobs; this.loading = false; },
      error: () => { this.error = 'Failed to load jobs.'; this.loading = false; },
    });
  }

  openCreate(): void { this.editingJob = null; this.showForm = true; }
  openEdit(job: JobDTO): void { this.editingJob = job; this.showForm = true; }
  closeForm(): void { this.showForm = false; this.editingJob = null; }

  onSaved(): void { this.closeForm(); this.loadJobs(); }

  promptDelete(id: number): void { this.confirmDeleteId = id; }
  cancelDelete(): void { this.confirmDeleteId = null; }

  confirmDelete(): void {
    if (this.confirmDeleteId == null) return;
    this.jobService.deleteJob(this.confirmDeleteId).subscribe({
      next: () => { this.confirmDeleteId = null; this.loadJobs(); },
      error: () => { this.error = 'Failed to delete job.'; this.confirmDeleteId = null; },
    });
  }
}
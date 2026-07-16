import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Candidateprofile } from '../../interface/candidateprofile';
import { Application } from '../../interface/application';
import { SavedJob } from '../../interface/saved-job';
import { JobDTO } from '../../interface/job-request';
import { Candidate } from '../../service/candidate';

@Component({
  selector: 'app-candidate-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit {

  profile: Candidateprofile | null = null;
  allJobs: JobDTO[] = [];
  filteredJobs: JobDTO[] = [];
  applications: Application[] = [];
  savedJobs: SavedJob[] = [];

  activeTab: 'jobs' | 'applications' | 'saved' = 'jobs';

  keyword = '';
  location = '';
  category = '';
  workMode = '';
  employmentType = '';

  loadingJobs = false;
  toastMessage = '';
  toastType: 'success' | 'error' = 'success';

  readonly categories = [
    'SOFTWARE_ENGINEERING', 'DATA_SCIENCE', 'PRODUCT_MANAGEMENT',
    'DESIGN', 'MARKETING', 'SALES', 'FINANCE', 'HR', 'OPERATIONS', 'OTHER'
  ];
  readonly workModes = ['REMOTE', 'HYBRID', 'ON_SITE'];
  readonly employmentTypes = ['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERNSHIP', 'FREELANCE'];

  constructor(private candidateService: Candidate) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadAllJobs();
    this.loadApplications();
    this.loadSavedJobs();
  }

  loadProfile(): void {
    this.candidateService.getProfile().subscribe({
      next: (data) => (this.profile = data),
      error: () => (this.profile = null)
    });
  }

  loadAllJobs(): void {
    this.loadingJobs = true;
    this.candidateService.getAllJobs().subscribe({
      next: (data) => {
        this.allJobs = data;
        this.filteredJobs = data;
        this.loadingJobs = false;
      },
      error: () => (this.loadingJobs = false)
    });
  }

  loadApplications(): void {
    this.candidateService.getApplications().subscribe({
      next: (data) => (this.applications = data)
    });
  }

  loadSavedJobs(): void {
    this.candidateService.getSavedJobs().subscribe({
      next: (data) => (this.savedJobs = data)
    });
  }

  searchJobs(): void {
    this.loadingJobs = true;
    this.candidateService.getJobs({
      keyword: this.keyword || undefined,
      location: this.location || undefined,
      category: this.category || undefined,
      workMode: this.workMode || undefined,
      employmentType: this.employmentType || undefined
    }).subscribe({
      next: (data) => {
        this.filteredJobs = data;
        this.loadingJobs = false;
      },
      error: () => (this.loadingJobs = false)
    });
  }

  clearFilters(): void {
    this.keyword = '';
    this.location = '';
    this.category = '';
    this.workMode = '';
    this.employmentType = '';
    this.filteredJobs = this.allJobs;
  }

  applyToJob(jobId: number): void {
    this.candidateService.applyJob(jobId).subscribe({
      next: () => {
        this.showToast('Application submitted!', 'success');
        this.loadApplications();
      },
      error: (err) => this.showToast(err?.error || 'Already applied or error occurred.', 'error')
    });
  }

  saveJob(jobId: number): void {
    this.candidateService.saveJob(jobId).subscribe({
      next: () => {
        this.showToast('Job saved!', 'success');
        this.loadSavedJobs();
      },
      error: (err) => this.showToast(err?.error || 'Could not save job.', 'error')
    });
  }

  unsaveJob(jobId: number): void {
    this.candidateService.unsaveJob(jobId).subscribe({
      next: () => {
        this.showToast('Removed from saved jobs.', 'success');
        this.loadSavedJobs();
      }
    });
  }

  withdrawApplication(applicationId: number): void {
    if (!confirm('Withdraw this application?')) return;
    this.candidateService.withdrawApplication(applicationId).subscribe({
      next: () => {
        this.showToast('Application withdrawn.', 'success');
        this.loadApplications();
      }
    });
  }

  isJobSaved(jobId: number): boolean {
    return this.savedJobs.some(sj => sj.jobId === jobId);
  }

  isJobApplied(jobId: number): boolean {
    return this.applications.some(a => a.jobId === jobId);
  }

  getProfileCompletion(): number {
    if (!this.profile) return 0;
    const fields: (keyof Candidateprofile)[] = [
      'fullName', 'phoneNumber', 'city', 'bio',
      'education', 'resumeUrl', 'profileImageUrl'
    ];
    const filled = fields.filter(f => !!this.profile![f]).length;
    return Math.round((filled / fields.length) * 100);
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      APPLIED: 'badge-applied',
      SHORTLISTED: 'badge-shortlisted',
      REJECTED: 'badge-rejected',
      HIRED: 'badge-hired',
      UNDER_REVIEW: 'badge-review'
    };
    return map[status] ?? 'badge-applied';
  }

  formatLabel(value: string): string {
    return value.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, c => c.toUpperCase());
  }

  private showToast(message: string, type: 'success' | 'error'): void {
    this.toastMessage = message;
    this.toastType = type;
    setTimeout(() => (this.toastMessage = ''), 3000);
  }
}
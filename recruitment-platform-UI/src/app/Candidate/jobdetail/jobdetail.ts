import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApplicationService } from '../../services/application/application-service';
import { JobService } from '../../services/job/job-service';
import { PublicJobResponse } from '../../models/job';
import { EnumLabelPipe } from '../../models/enum-label-pipe';
import { CommonModule } from '@angular/common';
import { PublicJobService } from '../../services/job/public-job-service';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ResumeService } from '../../services/resume/resume-service';
import { AiService } from '../../services/ai/ai-service';
import { ResumeResponse } from '../../models/resume';
import { JobMatchScoreResult, MissingSkillsResult, parseAnalysisResult } from '../../models/ai-analysis';

@Component({
  selector: 'app-jobdetail',
  imports: [RouterLink,CommonModule, EnumLabelPipe, ReactiveFormsModule],
  templateUrl: './jobdetail.html',
  styleUrl: './jobdetail.css',
})
export class Jobdetail implements OnInit{

  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly publicJobService = inject(PublicJobService);
  private readonly applicationService = inject(ApplicationService);
  private readonly savedJobService = inject(JobService);
  private readonly resumeService = inject(ResumeService);
  private readonly aiAnalysisService = inject(AiService);

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

  // --- AI match analysis ---
  readonly resumes = signal<ResumeResponse[]>([]);
  readonly isLoadingResumes = signal(true);
  readonly resumeForm = this.fb.nonNullable.group({ resumeId: [''] });

  readonly isCheckingMatch = signal(false);
  readonly matchError = signal<string | null>(null);
  readonly matchResult = signal<JobMatchScoreResult | null>(null);

  readonly isCheckingMissing = signal(false);
  readonly missingError = signal<string | null>(null);
  readonly missingSkills = signal<string[] | null>(null);

  ngOnInit(): void {
    this.loadJob();
    this.loadResumes();
  }

  loadJob(): void {
    this.isLoading.set(true);
    this.error.set(null);

    this.publicJobService.getById(this.jobId).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.job.set(res.data ?? null);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.error.set(err?.error?.message ?? 'Could not load this job. It may no longer be available.');
      },
    });
  }

  loadResumes(): void {
    this.isLoadingResumes.set(true);
    this.resumeService.getMyResumes().subscribe({
      next: (res) => {
        this.isLoadingResumes.set(false);
        const list = res.data ?? [];
        this.resumes.set(list);
        const primary = list.find((r) => r.primary) ?? list[0];
        if (primary) this.resumeForm.patchValue({ resumeId: primary.id });
      },
      error: () => {
        this.isLoadingResumes.set(false);
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
      error: (err) => {
        this.isApplying.set(false);
        this.applyError.set(err?.error?.message ?? 'Could not submit your application. Please try again.');
      },
    });
  }

  toggleSave(): void {
    this.isSaving.set(true);
    this.saveError.set(null);

    const onError = (err: any) => {
      this.isSaving.set(false);
      this.saveError.set(err?.error?.message ?? 'Could not update your saved jobs. Please try again.');
    };

    if (this.saved()) {
      this.savedJobService.unsave(this.jobId).subscribe({
        next: () => {
          this.isSaving.set(false);
          this.saved.set(false);
        },
        error: onError,
      });
    } else {
      this.savedJobService.save(this.jobId).subscribe({
        next: () => {
          this.isSaving.set(false);
          this.saved.set(true);
        },
        error: onError,
      });
    }
  }

  private selectedResumeId(): string | null {
    return this.resumeForm.getRawValue().resumeId || null;
  }

  checkMatchScore(): void {
    const resumeId = this.selectedResumeId();
    if (!resumeId) {
      this.matchError.set('Select a resume first.');
      return;
    }

    this.isCheckingMatch.set(true);
    this.matchError.set(null);

    this.aiAnalysisService.matchScore(resumeId, this.jobId).subscribe({
      next: (res) => {
        this.isCheckingMatch.set(false);
        if (!res.data) return;
        const parsed = parseAnalysisResult<JobMatchScoreResult>(res.data);
        if (!parsed) {
          this.matchError.set('The AI response could not be read. Please try again.');
          return;
        }
        this.matchResult.set(parsed);
      },
      error: (err) => {
        this.isCheckingMatch.set(false);
        this.matchError.set(err?.error?.message ?? 'Could not compute a match score. Please try again.');
      },
    });
  }

  checkMissingSkills(): void {
    const resumeId = this.selectedResumeId();
    if (!resumeId) {
      this.missingError.set('Select a resume first.');
      return;
    }

    this.isCheckingMissing.set(true);
    this.missingError.set(null);

    this.aiAnalysisService.missingSkills(resumeId, this.jobId).subscribe({
      next: (res) => {
        this.isCheckingMissing.set(false);
        if (!res.data) return;
        const parsed = parseAnalysisResult<MissingSkillsResult>(res.data);
        if (!parsed) {
          this.missingError.set('The AI response could not be read. Please try again.');
          return;
        }
        this.missingSkills.set(parsed.missingSkills);
      },
      error: (err) => {
        this.isCheckingMissing.set(false);
        this.missingError.set(err?.error?.message ?? 'Could not check missing skills. Please try again.');
      },
    });
  }
}
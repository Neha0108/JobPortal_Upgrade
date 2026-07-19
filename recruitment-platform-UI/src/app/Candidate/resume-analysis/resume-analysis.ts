import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AiService } from '../../services/ai/ai-service';
import { ResumeService } from '../../services/resume/resume-service';
import { ResumeResponse } from '../../models/resume';
import { parseAnalysisResult, SkillExtractionResult, SkillSummaryResult } from '../../models/ai-analysis';

@Component({
  selector: 'app-resume-analysis',
  imports: [],
  templateUrl: './resume-analysis.html',
  styleUrl: './resume-analysis.css',
})
export class ResumeAnalysis implements OnInit {

  private readonly route = inject(ActivatedRoute);
  private readonly aiAnalysisService = inject(AiService);
  private readonly resumeService = inject(ResumeService);

  private readonly resumeId = this.route.snapshot.paramMap.get('resumeId')!;

  readonly isLoadingResume = signal(true);
  readonly resume = signal<ResumeResponse | null>(null);
  readonly resumeError = signal<string | null>(null);

  readonly isExtractingSkills = signal(false);
  readonly skillsError = signal<string | null>(null);
  readonly skills = signal<string[] | null>(null);

  readonly isGeneratingSummary = signal(false);
  readonly summaryError = signal<string | null>(null);
  readonly summary = signal<string | null>(null);

  ngOnInit(): void {
    this.isLoadingResume.set(true);
    this.resumeService.getMyResumes().subscribe({
      next: (res) => {
        this.isLoadingResume.set(false);
        const match = (res.data ?? []).find((r) => r.id === this.resumeId);
        if (!match) {
          this.resumeError.set('This resume could not be found.');
          return;
        }
        this.resume.set(match);
      },
      error: (err) => {
        this.isLoadingResume.set(false);
        this.resumeError.set(err?.error?.message ?? 'Could not load this resume.');
      },
    });
  }

  extractSkills(): void {
    this.isExtractingSkills.set(true);
    this.skillsError.set(null);

    this.aiAnalysisService.extractSkills(this.resumeId).subscribe({
      next: (res) => {
        this.isExtractingSkills.set(false);
        if (!res.data) return;
        const parsed = parseAnalysisResult<SkillExtractionResult>(res.data);
        if (!parsed) {
          this.skillsError.set('The AI response could not be read. Please try again.');
          return;
        }
        this.skills.set(parsed.skills);
      },
      error: (err) => {
        this.isExtractingSkills.set(false);
        this.skillsError.set(err?.error?.message ?? 'Could not extract skills. Please try again.');
      },
    });
  }

  generateSummary(): void {
    this.isGeneratingSummary.set(true);
    this.summaryError.set(null);

    this.aiAnalysisService.generateSummary(this.resumeId).subscribe({
      next: (res) => {
        this.isGeneratingSummary.set(false);
        if (!res.data) return;
        const parsed = parseAnalysisResult<SkillSummaryResult>(res.data);
        if (!parsed) {
          this.summaryError.set('The AI response could not be read. Please try again.');
          return;
        }
        this.summary.set(parsed.summary);
      },
      error: (err) => {
        this.isGeneratingSummary.set(false);
        this.summaryError.set(err?.error?.message ?? 'Could not generate a summary. Please try again.');
      },
    });
  }
}
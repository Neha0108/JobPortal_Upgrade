import { Component, inject, OnInit, signal, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResumeService } from '../../services/resume/resume-service';
import { RESUME_ALLOWED_TYPES, RESUME_MAX_SIZE_BYTES, ResumeResponse } from '../../models/resume';
import { ToastService } from '../../shared/toast/toast-service';
import { LoadingState } from '../../shared/loading-state/loading-state';
import { EmptyState } from '../../shared/empty-state/empty-state';
import { ErrorState } from '../../shared/error-state/error-state';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-resumes',
  standalone: true,
  imports: [CommonModule,RouterLink, LoadingState, EmptyState, ErrorState],
  templateUrl: './resumes.html',
  styleUrl: './resumes.css',
})
export class Resumes implements OnInit {
  private readonly resumeService = inject(ResumeService);
  private readonly toast = inject(ToastService);

  @ViewChild('fileInput') fileInputRef?: ElementRef<HTMLInputElement>;

  readonly resumes = signal<ResumeResponse[]>([]);
  readonly isLoading = signal(true);
  readonly loadError = signal<string | null>(null);

  readonly isUploading = signal(false);
  /** Tracks which specific resume row has a set-primary/delete request in flight,
   *  so only that row's buttons disable rather than the whole list freezing. */
  readonly pendingActionId = signal<string | null>(null);

  ngOnInit(): void {
    this.loadResumes();
  }

  loadResumes(): void {
    this.isLoading.set(true);
    this.loadError.set(null);

    this.resumeService.getMyResumes().subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.resumes.set(res.data ?? []);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.loadError.set(err?.error?.message ?? 'Could not load your resumes. Please try again.');
      },
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    if (!RESUME_ALLOWED_TYPES.includes(file.type)) {
      this.toast.error('Only PDF and DOCX files are allowed.');
      this.resetFileInput();
      return;
    }
    if (file.size > RESUME_MAX_SIZE_BYTES) {
      this.toast.error('File exceeds the maximum allowed size of 5MB.');
      this.resetFileInput();
      return;
    }

    this.isUploading.set(true);
    this.resumeService.upload(file).subscribe({
      next: (res) => {
        this.isUploading.set(false);
        this.resetFileInput();
        if (res.data) {
          this.resumes.update((list) => [...list, res.data!]);
          this.toast.success('Resume uploaded.');
        }
      },
      error: (err) => {
        this.isUploading.set(false);
        this.resetFileInput();
        this.toast.error(err?.error?.message ?? 'Upload failed. Please try again.');
      },
    });
  }

  setPrimary(resume: ResumeResponse): void {
    if (resume.primary || this.pendingActionId()) return;

    this.pendingActionId.set(resume.id);
    this.resumeService.setPrimary(resume.id).subscribe({
      next: () => {
        this.pendingActionId.set(null);
        // Backend clears the flag on every other resume when one is set
        // primary - mirror that locally instead of re-fetching the whole list.
        this.resumes.update((list) =>
          list.map((r) => ({ ...r, primary: r.id === resume.id })),
        );
        this.toast.success(`"${resume.fileName}" is now your primary resume.`);
      },
      error: (err) => {
        this.pendingActionId.set(null);
        this.toast.error(err?.error?.message ?? 'Could not update primary resume.');
      },
    });
  }

  deleteResume(resume: ResumeResponse): void {
    if (this.pendingActionId()) return;
    const confirmed = window.confirm(`Delete "${resume.fileName}"? This cannot be undone.`);
    if (!confirmed) return;

    this.pendingActionId.set(resume.id);
    this.resumeService.delete(resume.id).subscribe({
      next: () => {
        this.pendingActionId.set(null);
        this.resumes.update((list) => list.filter((r) => r.id !== resume.id));
        this.toast.success('Resume deleted.');
      },
      error: (err) => {
        this.pendingActionId.set(null);
        this.toast.error(err?.error?.message ?? 'Could not delete resume.');
      },
    });
  }

  downloadResume(resume: ResumeResponse): void {
    this.resumeService.download(resume.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const anchor = document.createElement('a');
        anchor.href = url;
        anchor.download = resume.fileName;
        anchor.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => {
        this.toast.error('Could not download resume. Please try again.');
      },
    });
  }

  formatFileSize(bytes: number): string {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(0)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  }

  private resetFileInput(): void {
    if (this.fileInputRef) this.fileInputRef.nativeElement.value = '';
  }
}

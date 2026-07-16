import { Component, EventEmitter, Input, Output } from '@angular/core';
import { JobDTO } from '../../interface/job-request';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recommended-jobs',
  imports: [CommonModule],
  templateUrl: './recommended-jobs.html',
  styleUrl: './recommended-jobs.css',
})
export class RecommendedJobs {

  @Input() jobs: JobDTO[] = [];

  @Output() apply = new EventEmitter<number>();

  @Output() save = new EventEmitter<number>();

  @Output() details = new EventEmitter<number>();

  applyJob(jobId: number) {
    this.apply.emit(jobId);
  }

  saveJob(jobId: number) {
    this.save.emit(jobId);
  }

  openDetails(jobId: number) {
    this.details.emit(jobId);
  }

}
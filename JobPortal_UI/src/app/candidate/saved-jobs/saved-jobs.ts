import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SavedJob } from '../../interface/saved-job';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-saved-jobs',
  imports: [CommonModule],
  templateUrl: './saved-jobs.html',
  styleUrl: './saved-jobs.css',
})
export class SavedJobs {
  
  @Input() savedJobs: SavedJob[] = [];

  @Output() remove = new EventEmitter<number>();

  @Output() view = new EventEmitter<number>();

  removeJob(jobId: number) {
    this.remove.emit(jobId);
  }

  viewJob(jobId: number) {
    this.view.emit(jobId);
  }

}
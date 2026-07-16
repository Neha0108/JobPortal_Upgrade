import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Application } from '../../interface/application';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recent-applications',
  imports: [CommonModule],
  templateUrl: './recent-applications.html',
  styleUrl: './recent-applications.css',
})
export class RecentApplications {

  @Input() applications: Application[] = [];

  @Output() withdraw = new EventEmitter<number>();

  withdrawApplication(applicationId: number) {

    this.withdraw.emit(applicationId);

  }

}
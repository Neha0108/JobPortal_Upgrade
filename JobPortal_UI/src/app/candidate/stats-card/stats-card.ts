import { Component, Input, OnChanges } from '@angular/core';
import { Candidateprofile } from '../../interface/candidateprofile';

@Component({
  selector: 'app-stats-card',
  imports: [],
  templateUrl: './stats-card.html',
  styleUrl: './stats-card.css',
})
export class StatsCard implements OnChanges {
  @Input() applications = 0;

  @Input() savedJobs = 0;

  @Input() jobs = 0;

  @Input() profile!: Candidateprofile;

  profileCompletion = 0;

  ngOnChanges(): void {

    this.calculateProfileCompletion();

  }

  private calculateProfileCompletion(): void {

    if (!this.profile) {

      this.profileCompletion = 0;

      return;

    }

    let completed = 0;

    const totalFields = 10;

    if (this.profile.fullName) completed++;
    if (this.profile.phoneNumber) completed++;
    if (this.profile.bio) completed++;
    if (this.profile.education) completed++;
    if (this.profile.city) completed++;
    if (this.profile.state) completed++;
    if (this.profile.linkedinUrl) completed++;
    if (this.profile.githubUrl) completed++;
    if (this.profile.resumeUrl) completed++;
    if (this.profile.skills?.length) completed++;

    this.profileCompletion = Math.round((completed / totalFields) * 100);

  }

}
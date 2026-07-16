import { Component, Input } from '@angular/core';
import { Candidateprofile } from '../../interface/candidateprofile';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile-card',
  imports: [CommonModule],
  templateUrl: './profile-card.html',
  styleUrl: './profile-card.css',
})
export class ProfileCard {

  @Input() profile: Candidateprofile | null = null;

  getCompletion(): number {
    if (!this.profile) return 0;
    const fields: (keyof Candidateprofile)[] = [
      'fullName', 'phoneNumber', 'city', 'bio',
      'education', 'resumeUrl', 'profileImageUrl'
    ];
    return Math.round(fields.filter(f => !!this.profile![f]).length / fields.length * 100);
  }

  getInitials(): string {
    const name = this.profile?.fullName ?? '';
    return name.split(' ').slice(0, 2).map(n => n[0]).join('').toUpperCase() || '?';
  }
}
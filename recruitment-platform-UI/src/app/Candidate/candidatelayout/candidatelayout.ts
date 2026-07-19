import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth-service';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-candidatelayout',
  imports: [CommonModule, RouterOutlet, RouterLinkActive, RouterLink],
  templateUrl: './candidatelayout.html',
  styleUrl: './candidatelayout.css',
})
export class Candidatelayout {

  sidebarOpen = false;

  private readonly authService = inject(AuthService);

  readonly currentUser = this.authService.currentUser;

  logout(): void {
    this.authService.logout();
  }
}
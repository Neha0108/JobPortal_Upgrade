import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth/auth-service';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-recruiterlayout',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './recruiterlayout.html',
  styleUrl: './recruiterlayout.css',
})
export class Recruiterlayout {
  private readonly authService = inject(AuthService);

  readonly currentUser = this.authService.currentUser;

  sidebarOpen = false;

  logout(): void {
    this.authService.logout();
  }
}
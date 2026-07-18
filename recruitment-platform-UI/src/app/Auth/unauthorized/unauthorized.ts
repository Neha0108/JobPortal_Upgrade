import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="auth-page">
      <div class="stage-tracker">
        <span class="stage-tracker__node"></span>
        <span class="stage-tracker__line"></span>
        <span class="stage-tracker__node"></span>
        <span class="stage-tracker__line"></span>
        <span class="stage-tracker__node"></span>
        <span class="stage-tracker__line"></span>
        <span class="stage-tracker__node"></span>
      </div>
      <div class="auth-form">
        <h1>Access denied</h1>
        <p>You don't have permission to view this page.</p>
        <p class="auth-links">
          <a routerLink="/auth/login">Back to sign in</a>
        </p>
      </div>
    </div>
  `,
})
export class UnauthorizedComponent {}
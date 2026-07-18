import { Component, inject, OnInit, signal } from '@angular/core';
import { AuthService } from '../../services/auth/auth-service';
import { ActivatedRoute, RouterLink } from '@angular/router';

type VerifyState = 'verifying' | 'success' | 'error' | 'missing-token';

@Component({
  selector: 'app-verify-email',
  imports: [RouterLink],
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.css',
})
export class VerifyEmail implements OnInit {

  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);

  readonly state = signal<VerifyState>('verifying');
  readonly serverError = signal<string | null>(null);

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    if (!token) {
      this.state.set('missing-token');
      return;
    }

    this.authService.verifyEmail({ token }).subscribe({
      next: () => this.state.set('success'),
      error: (err) => {
        this.serverError.set(err?.error?.message ?? 'This verification link is invalid or has expired.');
        this.state.set('error');
      },
    });
  }
}

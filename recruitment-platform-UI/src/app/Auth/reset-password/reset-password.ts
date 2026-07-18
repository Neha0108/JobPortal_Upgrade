import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth-service';
import { ActivatedRoute, Router } from '@angular/router';
import { passwordPatternValidator, passwordsMatchValidator } from '../../Password.validator';

@Component({
  selector: 'app-reset-password',
  imports: [ReactiveFormsModule],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css',
})
export class ResetPassword {

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  private readonly token = this.route.snapshot.queryParamMap.get('token');

  readonly isSubmitting = signal(false);
  readonly serverError = signal<string | null>(null);
  readonly submitted = signal(false);
  readonly tokenMissing = signal(this.token === null);

  readonly form = this.fb.nonNullable.group(
    {
      newPassword: ['', [Validators.required, passwordPatternValidator()]],
      confirmPassword: ['', [Validators.required]],
    },
    { validators: passwordsMatchValidator('newPassword', 'confirmPassword') },
  );

  submit(): void {
    if (this.form.invalid || !this.token) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.serverError.set(null);

    this.authService
      .resetPassword({ token: this.token, newPassword: this.form.getRawValue().newPassword })
      .subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.submitted.set(true);
        },
        error: (err) => {
          this.isSubmitting.set(false);
          this.serverError.set(
            err?.error?.message ?? 'This reset link is invalid or has expired. Please request a new one.',
          );
        },
      });
  }

  goToLogin(): void {
    this.router.navigateByUrl('/auth/login');
  }
}
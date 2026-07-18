import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth-service';
import { Router, RouterLink } from '@angular/router';
import { Role } from '../../models/role.enum';
import { passwordPatternValidator, passwordsMatchValidator } from '../../Password.validator';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly Role = Role;
  readonly isSubmitting = signal(false);
  readonly serverError = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group(
    {
      fullName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, passwordPatternValidator()]],
      confirmPassword: ['', [Validators.required]],
      role: [Role.CANDIDATE, [Validators.required]],
    },
    { validators: passwordsMatchValidator() },
  );

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.serverError.set(null);

    const { confirmPassword, ...raw } = this.form.getRawValue();
    const request = raw as typeof raw & { role: Role.RECRUITER | Role.CANDIDATE };

    this.authService.register(request).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.router.navigate(['/auth/login'], { queryParams: { registered: 'true' } });
      },
      error: (err) => {
        this.isSubmitting.set(false);
        this.serverError.set(err?.error?.message ?? 'Registration failed. Please try again.');
      },
    });
  }
}
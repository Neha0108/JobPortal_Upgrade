import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../../services/auth/auth-service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Role } from '../../models/role.enum';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  readonly isSubmitting = signal(false);
  readonly serverError = signal<string | null>(null);
  readonly justRegistered = signal(this.route.snapshot.queryParamMap.get('registered') === 'true');

  readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.serverError.set(null);

    this.authService.login(this.form.getRawValue()).subscribe({
      next: (res) => {
        this.isSubmitting.set(false);
        const role = res.data?.role;
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
        this.router.navigateByUrl(returnUrl ?? this.defaultRouteFor(role));
      },
      error: (err) => {
        this.isSubmitting.set(false);
        this.serverError.set(err?.error?.message ?? 'Login failed. Please check your credentials.');
      },
    });
  }

  private defaultRouteFor(role: Role | undefined): string {
    switch (role) {
      case Role.ADMIN:
        return '/admin';
      case Role.RECRUITER:
        return '/recruiter';
      case Role.CANDIDATE:
        return '/candidate';
      default:
        return '/auth/login';
    }
  }
}

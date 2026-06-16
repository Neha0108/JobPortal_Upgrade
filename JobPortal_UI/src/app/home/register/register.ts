import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { AuthService } from '../../service/auth-service';
import { RegisterRequest } from '../../interface/register-request';
import { AuthResponse } from '../../interface/auth-response';

@Component({
  selector: 'app-register',
  imports: [ ReactiveFormsModule,RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  registerForm = this.fb.group({

    username: [
      '',
      [
        Validators.required,
        Validators.minLength(3)
      ]
    ],

    email: [
      '',
      [
        Validators.required,
        Validators.email
      ]
    ],

    password: [
      '',
      [
        Validators.required,
        Validators.minLength(6)
      ]
    ],

    role: [
      '',
      Validators.required
    ]
  });

  selectRole(role: string) {

  this.registerForm.patchValue({
    role: role
  });
}

  onSubmit() {

    if (this.registerForm.invalid) {
      return;
    }

    const request: RegisterRequest = {

      username:
        this.registerForm.value.username ?? '',

      email:
        this.registerForm.value.email ?? '',

      password:
        this.registerForm.value.password ?? '',

      role: this.registerForm.value.role as any
    };

    this.authService
      .register(request)
      .subscribe({

        next: (res: AuthResponse) => {

          localStorage.setItem(
            'accessToken',
            res.accessToken
          );

          localStorage.setItem(
            'refreshToken',
            res.refreshToken
          );

          localStorage.setItem(
            'role',
            res.role
          );

          localStorage.setItem(
            'userId',
            res.userId.toString()
          );

          if (
            res.role === 'ROLE_CANDIDATE'
          ) {

            this.router.navigate([
              '/candidate/dashboard'
            ]);

          } else {

            this.router.navigate([
              '/recruiter/dashboard'
            ]);
          }
        },

        error: (err) => {

          console.error(
            'Registration Failed',
            err
          );
        }
      });
  }
}
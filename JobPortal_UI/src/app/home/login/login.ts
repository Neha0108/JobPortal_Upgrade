import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';

import { AuthService } from '../../service/auth-service';
import { LoginRequest } from '../../interface/login-request';
import { AuthResponse } from '../../interface/auth-response';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);

  loginForm = this.fb.group({
    email: [''],
    password: ['']
  });

  onSubmit() {

    const request: LoginRequest = {
      email: this.loginForm.value.email ?? '',
      password: this.loginForm.value.password ?? ''
    };

    this.authService.login(request).subscribe({
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

          if (res.role === 'CANDIDATE') {
            this.router.navigate(['/']);
          } else {
            this.router.navigate(['/register']);
          }

          console.log('Login successful:', res);
        },

        error: (err) => {
          console.error(err);
        }
      });
  }
}
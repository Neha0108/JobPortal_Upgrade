import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

import {
  AuthResponse,
  AuthenticatedUser,
  ForgotPasswordRequest,
  LoginRequest,
  RefreshTokenRequest,
  RegisterRequest,
  ResetPasswordRequest,
  VerifyEmailRequest,
} from '../../models/auth';
import { environment } from '../../../env/environment';
import { ApiResponse } from '../../models/api-response';
import { TokenStorageService } from '../token/token-storage';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenStorage = inject(TokenStorageService);
  private readonly router = inject(Router);
  private readonly baseUrl = `${environment.apiBaseUrl}/auth`;

  private readonly _currentUser = signal<AuthenticatedUser | null>(this.tokenStorage.getUser());

  readonly currentUser = this._currentUser.asReadonly();
  readonly isAuthenticated = computed(() => this._currentUser() !== null);
  readonly currentRole = computed(() => this._currentUser()?.role ?? null);

  login(request: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}/login`, request).pipe(
      tap((res) => this.persistSession(res.data)),
    );
  }

  register(request: RegisterRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}/register`, request).pipe(
      tap((res) => this.persistSession(res.data)),
    );
  }

  /** Used directly by callers that want the raw response; the interceptor calls the storage-only refresh path below. */
  refresh(refreshToken: string): Observable<ApiResponse<AuthResponse>> {
    const body: RefreshTokenRequest = { refreshToken };
    return this.http.post<ApiResponse<AuthResponse>>(`${this.baseUrl}/refresh`, body).pipe(
      tap((res) => this.persistSession(res.data)),
    );
  }

  logout(): void {
    const refreshToken = this.tokenStorage.getRefreshToken();
    const finish = () => {
      this.tokenStorage.clear();
      this._currentUser.set(null);
      this.router.navigateByUrl('/auth/login');
    };

    if (!refreshToken) {
      finish();
      return;
    }

    const body: RefreshTokenRequest = { refreshToken };
    // Best-effort server-side revocation; local state clears regardless of outcome.
    this.http.post<ApiResponse<void>>(`${this.baseUrl}/logout`, body).subscribe({
      next: finish,
      error: finish,
    });
  }

  forgotPassword(request: ForgotPasswordRequest): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.baseUrl}/forgot-password`, request);
  }

  resetPassword(request: ResetPasswordRequest): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.baseUrl}/reset-password`, request);
  }

  verifyEmail(request: VerifyEmailRequest): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(`${this.baseUrl}/verify-email`, request);
  }

  private persistSession(data: AuthResponse | undefined): void {
    if (!data) return;
    this.tokenStorage.save(data);
    this._currentUser.set({ userId: data.userId, email: data.email, role: data.role });
  }
}
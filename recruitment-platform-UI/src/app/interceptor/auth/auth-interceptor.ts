import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { TokenStorageService } from '../../services/token/token-storage';
import { environment } from '../../../env/environment';
import { ApiResponse } from '../../models/api-response';
import { AuthResponse } from '../../models/auth';
// Module-level (not injected) so state survives across requests without
// needing a dedicated singleton service just for this flag.
let isRefreshing = false;
const refreshedToken$ = new BehaviorSubject<string | null>(null);

const AUTH_ENDPOINTS = ['/auth/login', '/auth/register', '/auth/refresh', '/auth/logout'];

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenStorage = inject(TokenStorageService);
  const http = inject(HttpClient);
  const router = inject(Router);

  const isAuthCall = AUTH_ENDPOINTS.some((path) => req.url.includes(path));
  const accessToken = tokenStorage.getAccessToken();

  const authorizedReq = accessToken && !isAuthCall
    ? req.clone({ setHeaders: { Authorization: `Bearer ${accessToken}` } })
    : req;

  return next(authorizedReq).pipe(
    catchError((error: unknown) => {
      const is401 = error instanceof HttpErrorResponse && error.status === 401;
      if (!is401 || isAuthCall) {
        return throwError(() => error);
      }
      return handle401(req, next, tokenStorage, http, router);
    }),
  );
};

function handle401(
  req: Parameters<HttpInterceptorFn>[0],
  next: Parameters<HttpInterceptorFn>[1],
  tokenStorage: TokenStorageService,
  http: HttpClient,
  router: Router,
) {
  const refreshToken = tokenStorage.getRefreshToken();
  if (!refreshToken) {
    forceLogout(tokenStorage, router);
    return throwError(() => new Error('Session expired.'));
  }

  if (!isRefreshing) {
    isRefreshing = true;
    refreshedToken$.next(null);

    return http
      .post<ApiResponse<AuthResponse>>(`${environment.apiBaseUrl}/auth/refresh`, { refreshToken })
      .pipe(
        switchMap((res) => {
          isRefreshing = false;
          const data = res.data;
          if (!data) {
            forceLogout(tokenStorage, router);
            return throwError(() => new Error('Refresh failed.'));
          }
          tokenStorage.save(data);
          refreshedToken$.next(data.accessToken);
          return next(req.clone({ setHeaders: { Authorization: `Bearer ${data.accessToken}` } }));
        }),
        catchError((err) => {
          isRefreshing = false;
          forceLogout(tokenStorage, router);
          return throwError(() => err);
        }),
      );
  }

  // A refresh is already in flight (e.g. two parallel requests both got 401) —
  // queue behind it instead of firing a second /auth/refresh call.
  return refreshedToken$.pipe(
    filter((token): token is string => token !== null),
    take(1),
    switchMap((token) => next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }))),
  );
}

function forceLogout(tokenStorage: TokenStorageService, router: Router): void {
  tokenStorage.clear();
  router.navigateByUrl('/auth/login');
}
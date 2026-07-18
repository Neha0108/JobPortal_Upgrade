import { CanActivateFn } from '@angular/router';

import { inject } from '@angular/core';
import { AuthService } from '../services/auth/auth-service';
import { Router } from '@angular/router';
import { Role } from '../models/role.enum';

/**
 * Usage in routes: { path: 'admin', canActivate: [authGuard, roleGuard([Role.ADMIN])], ... }
 * Runs after authGuard, so an unauthenticated user is redirected to login first.
 */
export const roleGuard = (allowedRoles: Role[]): CanActivateFn => {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const role = authService.currentRole();
    if (role && allowedRoles.includes(role)) {
      return true;
    }

    return router.createUrlTree(['/unauthorized']);
  };
};
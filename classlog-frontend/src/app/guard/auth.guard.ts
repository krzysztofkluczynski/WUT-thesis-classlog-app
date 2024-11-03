import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../service/auth/auth.service';
import { ErrorResponse } from '../model/error-response.model';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const expectedRole = route.data['role'];
  const userRole = authService.getUserRole();

  if (expectedRole && userRole !== expectedRole) {
    throw new ErrorResponse(403, 'Unauthorized access', { requiredRole: expectedRole });
  }

  return true;
};

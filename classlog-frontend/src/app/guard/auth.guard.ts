import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../service/auth/auth.service';
import { ErrorResponse } from '../model/error-response.model';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const expectedRole = route.data['role'];

  if (authService.isAuthenticated() && authService.getUserRole() === expectedRole) {
    return true;
  } else {
    throw new ErrorResponse(403, 'Unauthorized access', { requiredRole: expectedRole });
  }
};

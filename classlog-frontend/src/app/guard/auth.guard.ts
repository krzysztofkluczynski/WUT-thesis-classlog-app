import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../service/auth/auth.service';
import {GlobalNotificationHandler} from "../service/notification/global-notification-handler.service";
import {ErrorResponse} from "../model/error/error-response";

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const globalNotificationHandler = inject(GlobalNotificationHandler);

  if (!authService.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  const expectedRole = route.data['role'];
  const userRole = authService.getUserRole();

  if (expectedRole && userRole !== expectedRole) {
    globalNotificationHandler.handleError('Unauthorized access');
    throw new ErrorResponse(403, 'Unauthorized access, requires role', {requiredRole: expectedRole});
  }
  return true;
};

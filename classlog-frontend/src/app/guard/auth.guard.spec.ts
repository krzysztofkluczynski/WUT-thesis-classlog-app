import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';
import { authGuard } from './auth.guard';
import { AuthService } from '../service/auth/auth.service';
import { Router } from '@angular/router';
import { GlobalNotificationHandler } from '../service/notification/global-notification-handler.service';
import {ErrorResponse} from "../model/error/error-response";

describe('authGuard', () => {
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockRouter: jasmine.SpyObj<Router>;
  let mockNotificationHandler: jasmine.SpyObj<GlobalNotificationHandler>;

  const executeGuard: CanActivateFn = (...guardParameters) =>
    TestBed.runInInjectionContext(() => authGuard(...guardParameters));

  beforeEach(() => {
    mockAuthService = jasmine.createSpyObj('AuthService', ['isAuthenticated', 'getUserRole']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);
    mockNotificationHandler = jasmine.createSpyObj('GlobalNotificationHandler', ['handleError']);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
      ],
    });
  });

  it('should allow access when authenticated and role matches', () => {
    mockAuthService.isAuthenticated.and.returnValue(true);
    mockAuthService.getUserRole.and.returnValue('admin');

    const result = executeGuard({ data: { role: 'admin' } } as any, {} as any);

    expect(result).toBeTrue();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

  it('should deny access and navigate to login if not authenticated', () => {
    mockAuthService.isAuthenticated.and.returnValue(false);

    const result = executeGuard({ data: { role: 'admin' } } as any, {} as any);

    expect(result).toBeFalse();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should deny access and throw ErrorResponse if role does not match', () => {
    mockAuthService.isAuthenticated.and.returnValue(true);
    mockAuthService.getUserRole.and.returnValue('user');

    expect(() => {
      executeGuard({ data: { role: 'admin' } } as any, {} as any);
    }).toThrowMatching((error) => {
      return error instanceof ErrorResponse &&
        error.statusCode === 403 &&
        error.message === 'Unauthorized access' &&
        error.details.requiredRole === 'admin';
    });

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith('Unauthorized access');
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });



  it('should allow access when no role is specified', () => {
    mockAuthService.isAuthenticated.and.returnValue(true);

    const result = executeGuard({ data: {} } as any, {} as any);

    expect(result).toBeTrue();
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });
});

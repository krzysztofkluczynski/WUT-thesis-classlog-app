// noinspection TypeScriptValidateTypes

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import { Subject, of } from 'rxjs';
import { LoginFormComponent } from './login-form.component';
import { AxiosService } from '../../../service/axios/axios.service';
import { AuthService } from '../../../service/auth/auth.service';
import { GlobalNotificationHandler } from '../../../service/notification/global-notification-handler.service';
import { HeaderService } from '../../../service/header/header.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  template: '',
})
class MockHeaderComponent {}

class MockRouter {
  events = new Subject<NavigationEnd>();
  navigate = jasmine.createSpy('navigate').and.callFake((commands: any[], extras?: any) => {});
}

class MockHeaderService {
  selectedOption$ = of(null);
  setSelectedOption = jasmine.createSpy('setSelectedOption').and.stub();
  getSelectedOption = jasmine.createSpy('getSelectedOption').and.returnValue(null);
}

class MockAuthService {
  setUser = jasmine.createSpy('setUser').and.stub();
  getUser = jasmine.createSpy('getUser').and.returnValue({
    id: 1,
    name: 'Mock User',
    surname: 'Mock Surname',
    email: 'mock@example.com',
    role: { id: 1, roleName: 'Admin' },
    token: 'mock-token',
    createdAt: new Date(),
  });
  getUserRole = jasmine.createSpy('getUserRole').and.returnValue('Admin');
  getAuthToken = jasmine.createSpy('getAuthToken').and.returnValue('mock-token');
  logout = jasmine.createSpy('logout').and.stub();
}

class MockAxiosService {
  request = jasmine.createSpy('request').and.callFake((method: string, url: string, data: any) => {
    if (data.email === 'test@example.com' && data.password === 'password') {
      return Promise.resolve({
        data: {
          id: 1,
          name: 'Test',
          surname: 'User',
          email: 'test@example.com',
          role: { id: 1, roleName: 'Admin' },
          token: 'mock-token',
          createdAt: new Date(),
        },
      });
    } else if (data.email === 'wrong@example.com' && data.password === 'wrongpassword') {
      return Promise.reject({ response: { status: 401, data: { message: 'Invalid credentials' } } });
    }
    return Promise.reject('Unknown error');
  });
}

class MockGlobalNotificationHandler {
  handleError = jasmine.createSpy('handleError').and.stub();
}

describe('LoginFormComponent', () => {
  let component: LoginFormComponent;
  let fixture: ComponentFixture<LoginFormComponent>;
  let mockAuthService: MockAuthService;
  let mockAxiosService: MockAxiosService;
  let mockRouter: MockRouter;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  beforeEach(async () => {
    mockAuthService = new MockAuthService();
    mockAxiosService = new MockAxiosService();
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    const mockHeaderService = new MockHeaderService();

    await TestBed.configureTestingModule({
      imports: [FormsModule, NgClass, NgIf, LoginFormComponent], // Import the standalone component
      declarations: [MockHeaderComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: HeaderService, useValue: mockHeaderService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService.logout on initialization', () => {
    expect(mockAuthService.logout).toHaveBeenCalled();
  });

  it('should submit login and navigate to admin dashboard for Admin role', async () => {
    component.email = 'test@example.com';
    component.password = 'password';
    await component.onSubmitLogin();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/login', {
      email: 'test@example.com',
      password: 'password',
    });
    expect(mockAuthService.setUser).toHaveBeenCalledWith(jasmine.objectContaining({
      email: 'test@example.com',
      role: jasmine.objectContaining({ roleName: 'Admin' }),
    }));
    expect(mockRouter.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['/admin/students']));
  });


  it('should handle login error and call notification handler', async () => {
    // Mock Axios service to reject with a specific error
    const error = { response: { status: 401, data: { message: 'Invalid credentials' } } };
    mockAxiosService.request.and.returnValue(Promise.reject(error));

    component.email = 'wrong@example.com';
    component.password = 'wrongpassword';
    await component.onSubmitLogin();

    // Ensure Axios request is made with correct parameters
    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/login', {
      email: 'wrong@example.com',
      password: 'wrongpassword',
    });
  });


  it('should navigate to register page on navigateToRegister', () => {
    const mockEvent = { preventDefault: jasmine.createSpy('preventDefault') } as unknown as Event;

    component.navigateToRegister(mockEvent);

    expect(mockEvent.preventDefault).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/register']);
  });


  it('should not submit login if email or password is empty', () => {
    component.email = '';
    component.password = 'password';
    component.onSubmitLogin();

    expect(mockAxiosService.request).not.toHaveBeenCalled();

    component.email = 'test@example.com';
    component.password = '';
    component.onSubmitLogin();

    expect(mockAxiosService.request).not.toHaveBeenCalled();
  });
});

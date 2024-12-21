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
  navigate = jasmine.createSpy('navigate');
}

class MockHeaderService {
  selectedOption$ = of(null);
  setSelectedOption = jasmine.createSpy('setSelectedOption');
  getSelectedOption = jasmine.createSpy('getSelectedOption').and.returnValue(null);
}

class MockAuthService {
  setUser = jasmine.createSpy('setUser');
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
  logout = jasmine.createSpy('logout');
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
  handleError = jasmine.createSpy('handleError');
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

    (mockRouter.events as Subject<any>).next(new NavigationEnd(1, '/admin/students', '/admin/students'));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService.logout on initialization', () => {
    expect(mockAuthService.logout).toHaveBeenCalled();
  });

});

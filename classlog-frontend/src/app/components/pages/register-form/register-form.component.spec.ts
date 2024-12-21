import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { AxiosService } from '../../../service/axios/axios.service';
import { AuthService } from '../../../service/auth/auth.service';
import { GlobalNotificationHandler } from '../../../service/notification/global-notification-handler.service';
import {RegisterFormComponent} from "./register-form.component";
import {createMockUserDto} from "../../../utils/create-mock-user";
import {Component} from "@angular/core";
import {LoginFormComponent} from "../login-form/login-form.component";
import {HeaderService} from "../../../service/header/header.service";


@Component({
  selector: 'app-header',
  template: '' // Empty template as a mock
})
class MockHeaderComponent {}


class MockHeaderService {
  selectedOption$ = of(null);
  setSelectedOption = jasmine.createSpy('setSelectedOption').and.stub();
  getSelectedOption = jasmine.createSpy('getSelectedOption').and.returnValue(null);
}

class MockRouter {
  navigate = jasmine.createSpy('navigate').and.callFake((commands: any[], extras?: any) => {});
}

class MockAuthService {
  setUser = jasmine.createSpy('setUser').and.stub();
  getUser = jasmine.createSpy('getUser').and.returnValue(createMockUserDto(1, 'Mock', 'User'));
  getAuthToken = jasmine.createSpy('getAuthToken').and.returnValue('mock-token');
}

class MockAxiosService {
  request = jasmine.createSpy('request').and.callFake((method: string, url: string, data: any) => {
    if (url === '/register' && method === 'POST') {
      return Promise.resolve({ data: { id: 1, name: 'Test', surname: 'User', email: 'test@example.com' } });
    }
    if (url === '/login' && method === 'POST') {
      return Promise.resolve({ data: { id: 1, name: 'Test', surname: 'User', email: 'test@example.com' } });
    }
    return Promise.reject({ response: { status: 400, data: { message: 'Error' } } });
  });
}

class MockGlobalNotificationHandler {
  handleMessage = jasmine.createSpy('handleMessage').and.stub();
  handleError = jasmine.createSpy('handleError').and.stub();
}

describe('RegisterFormComponent', () => {
  let component: RegisterFormComponent;
  let fixture: ComponentFixture<RegisterFormComponent>;
  let mockRouter: MockRouter;
  let mockAuthService: MockAuthService;
  let mockAxiosService: MockAxiosService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  beforeEach(async () => {
    mockAuthService = new MockAuthService();
    mockAxiosService = new MockAxiosService();
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    const mockHeaderService = new MockHeaderService();

    await TestBed.configureTestingModule({
      imports: [FormsModule, NgClass, NgIf, RegisterFormComponent], // Import the standalone component
      declarations: [MockHeaderComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: HeaderService, useValue: mockHeaderService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not submit register if passwords do not match', () => {
    component.password = 'password';
    component.confirmPassword = 'differentPassword';
    component.checkPasswordMatch();

    expect(component.passwordMismatch).toBeTrue();
    component.onSubmitRegister();
    expect(mockAxiosService.request).not.toHaveBeenCalled();
  });

  it('should submit register and navigate to unknown route on success', async () => {
    component.name = 'Test';
    component.surname = 'User';
    component.email = 'test@example.com';
    component.password = 'password';
    component.confirmPassword = 'password';

    await component.onSubmitRegister();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/register', {
      name: 'Test',
      surname: 'User',
      email: 'test@example.com',
      password: 'password',
    });
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Registration successful');
  });

  it('should handle registration failure gracefully', async () => {
    mockAxiosService.request.and.callFake((method, url) => {
      if (url === '/register') {
        return Promise.reject({ response: { status: 400, data: { message: 'Registration failed' } } });
      }
      return Promise.resolve({});
    });

    component.name = 'Test';
    component.surname = 'User';
    component.email = 'test@example.com';
    component.password = 'password';
    component.confirmPassword = 'password';

    await component.onSubmitRegister();

    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });


  it('should navigate to login page when navigateToLogin is called', () => {
    const mockEvent = { preventDefault: jasmine.createSpy('preventDefault') } as unknown as Event;

    component.navigateToLogin(mockEvent);

    expect(mockEvent.preventDefault).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should update password mismatch flag correctly', () => {
    component.password = 'password';
    component.confirmPassword = 'password';
    component.checkPasswordMatch();
    expect(component.passwordMismatch).toBeFalse();

    component.confirmPassword = 'differentPassword';
    component.checkPasswordMatch();
    expect(component.passwordMismatch).toBeTrue();
  });
});

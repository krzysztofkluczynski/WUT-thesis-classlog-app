import {EMPTY, Observable, of, Subject} from 'rxjs';
import { Component } from '@angular/core';
import {createMockUserDto} from "../create-mock-user";
import {HeaderOptions} from "../header-options";

// Mock HeaderComponent
@Component({
  selector: 'app-header',
  template: '', // Empty mock template
})
export class MockHeaderComponent {}

// Mock HeaderService
export class MockHeaderService {
  selectedOption$ = of<HeaderOptions | null>(null); // Allow HeaderOptions or null
  setSelectedOption = jasmine.createSpy('setSelectedOption').and.stub();
  getSelectedOption = jasmine.createSpy('getSelectedOption').and.returnValue(null);
}


// Mock Router
export class MockRouter {
  navigate = jasmine.createSpy('navigate').and.callFake((commands: any[], extras?: any) => {});
  navigateByUrl = jasmine.createSpy('navigateByUrl').and.callFake((url: string, extras?: any) => {
    return Promise.resolve(true); // Simulate successful navigation
  });
  events: Observable<any> = EMPTY; // Provide a default empty observable
}

// Mock AuthService
export class MockAuthService {
  setUser = jasmine.createSpy('setUser').and.stub();
  getUser = jasmine.createSpy('getUser').and.returnValue({
    id: 1,
    name: 'Mock User',
    surname: 'Mock Surname',
    email: 'mock@example.com',
    role: { id: 1 },
  });
  getAuthToken = jasmine.createSpy('getAuthToken').and.returnValue('mock-token');
  logout = jasmine.createSpy('logout').and.stub();
  getUserWithoutToken = jasmine.createSpy('getUserWithoutToken').and.returnValue(createMockUserDto(1, 'Mock User', 'Mock Surname'));
}

// Mock AxiosService
export class MockAxiosService {
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

// Mock GlobalNotificationHandler
export class MockGlobalNotificationHandler {
  handleMessage = jasmine.createSpy('handleMessage');
  handleError = jasmine.createSpy('handleError');
}

import { of } from 'rxjs';
import { Component } from '@angular/core';

// Mock HeaderComponent
@Component({
  selector: 'app-header',
  template: '', // Empty mock template
})
export class MockHeaderComponent {}

// Mock HeaderService
export class MockHeaderService {
  selectedOption$ = of(null);
  setSelectedOption = jasmine.createSpy('setSelectedOption').and.stub();
  getSelectedOption = jasmine.createSpy('getSelectedOption').and.returnValue(null);
}

// Mock Router
export class MockRouter {
  navigate = jasmine.createSpy('navigate').and.callFake((commands: any[], extras?: any) => {});
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
  handleMessage = jasmine.createSpy('handleMessage').and.stub();
  handleError = jasmine.createSpy('handleError').and.stub();
}

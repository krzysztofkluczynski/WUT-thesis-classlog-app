import {ComponentFixture, TestBed} from '@angular/core/testing';
import {UserProfileComponent} from './user-profile.component';
import {ActivatedRoute, Router} from '@angular/router';
import {AxiosService} from '../../../service/axios/axios.service';
import {AuthService} from '../../../service/auth/auth.service';
import {GlobalNotificationHandler} from '../../../service/notification/global-notification-handler.service';
import {MockAuthService, MockGlobalNotificationHandler, MockRouter} from '../../../utils/tests/test-commons';
import {UserDto} from '../../../model/entities/user-dto';
import {of} from 'rxjs';

describe('UserProfileComponent', () => {
  let component: UserProfileComponent;
  let fixture: ComponentFixture<UserProfileComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockRouter: MockRouter;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockAuthService: MockAuthService;

  const mockUser: UserDto = {
    id: 1,
    name: 'John',
    surname: 'Doe',
    email: 'john.doe@example.com',
    role: {id: 2, roleName: 'Student'},
    token: '',
    createdAt: new Date('2023-01-01T10:00:00'),
  };

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockAuthService = new MockAuthService();

    mockAxiosService.request.and.callFake((method: string, url: string, payload?: any) => {
      if (url.includes(`/users/1`) && method === 'GET') {
        return Promise.resolve({data: mockUser});
      }
      if (url.includes(`/users/1`) && method === 'PUT') {
        return Promise.resolve({data: {...mockUser, ...payload}});
      }
      if (url.includes(`/users/1`) && method === 'DELETE') {
        return Promise.resolve({data: 'User deleted successfully'});
      }
      if (url.includes('/users/change-password') && method === 'POST') {
        return Promise.resolve({data: 'Password changed successfully'});
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [UserProfileComponent],
      providers: [
        {provide: AxiosService, useValue: mockAxiosService},
        {provide: AuthService, useValue: mockAuthService},
        {provide: Router, useValue: mockRouter},
        {provide: GlobalNotificationHandler, useValue: mockNotificationHandler},
        {
          provide: ActivatedRoute,
          useValue: {snapshot: {paramMap: {get: () => '1'}}, queryParams: of({editMode: 'false'})}
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(UserProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });


  it('should toggle edit mode', () => {
    expect(component.editButtonClicked).toBeFalse();
    component.editUser();
    expect(component.editButtonClicked).toBeTrue();
  });

  it('should save changes', async () => {
    component.userDto.name = 'Jane';
    component.editButtonClicked = true;

    await component.saveChanges();

    expect(mockAxiosService.request).toHaveBeenCalledWith('PUT', '/users/1', jasmine.any(Object));
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('User updated successfully');
    expect(component.editButtonClicked).toBeFalse();
  });

  it('should handle password change', async () => {
    component.currentPassword = 'old-password';
    component.newPassword = 'new-password';
    component.confirmNewPassword = 'new-password';
    component.changePasswordClicked = true;

    await component.saveChanges();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/users/change-password', {
      userId: 1,
      oldPassword: 'old-password',
      newPassword: 'new-password',
    });
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('User changed password');
  });

  it('should handle mismatched passwords', () => {
    component.currentPassword = 'old-password';
    component.newPassword = 'new-password';
    component.confirmNewPassword = 'mismatch-password';
    component.changePasswordClicked = true;
    component.userDto = mockUser;

    component.saveChanges();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith('Passwords do not match.');
    expect(component.editButtonClicked).toBeFalse();
  });

  it('should delete user', async () => {
    await component.deleteUser();

    expect(mockAxiosService.request).toHaveBeenCalledWith('DELETE', '/users/1', {});
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('User deleted successfully');
  });

  it('should cancel changes', () => {
    component.editButtonClicked = true;
    component.currentPassword = 'old-password';
    component.newPassword = 'new-password';
    component.confirmNewPassword = 'mismatch-password';

    component.cancelChanges();

    expect(component.editButtonClicked).toBeFalse();
    expect(component.changePasswordClicked).toBeFalse();
    expect(component.currentPassword).toBe('');
    expect(component.newPassword).toBe('');
    expect(component.confirmNewPassword).toBe('');
  });

  it('should toggle change password mode', () => {
    expect(component.changePasswordClicked).toBeFalse();

    component.toggleChangePassword();

    expect(component.changePasswordClicked).toBeTrue();

    component.toggleChangePassword();

    expect(component.changePasswordClicked).toBeFalse();
  });
});

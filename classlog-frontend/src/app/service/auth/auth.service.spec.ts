import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { UserDto } from '../../model/entities/user-dto';
import { createMockUserDto } from '../../utils/create-mock-user';

describe('AuthService', () => {
  let service: AuthService;

  const mockUser: UserDto = createMockUserDto(1, 'mockUser@example.com', 'admin');

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);

    // Mocking sessionStorage
    spyOn(window.sessionStorage, 'getItem').and.callFake((key: string) => {
      if (key === 'user_data') {
        return JSON.stringify(mockUser);
      }
      return null;
    });

    spyOn(window.sessionStorage, 'setItem');
    spyOn(window.sessionStorage, 'removeItem');
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set user and store it in sessionStorage', () => {
    service.setUser(mockUser);
    expect(window.sessionStorage.setItem).toHaveBeenCalledWith(
      'user_data',
      JSON.stringify(mockUser)
    );
  });

  it('should clear user data when setUser is called with null', () => {
    service.setUser(null);
    expect(window.sessionStorage.removeItem).toHaveBeenCalledWith('user_data');
  });

  it('should return null if no user data in sessionStorage', () => {
    (window.sessionStorage.getItem as jasmine.Spy).and.returnValue(null);
    const user = service.getUser();
    expect(user).toBeNull();
  });

  it('should retrieve auth token', () => {
    const token = service.getAuthToken();
    expect(token).toEqual(mockUser.token);
  });

  it('should return null for auth token if no user is set', () => {
    (window.sessionStorage.getItem as jasmine.Spy).and.returnValue(null);
    const token = service.getAuthToken();
    expect(token).toBeNull();
  });

  it('should retrieve user role', () => {
    const role = service.getUserRole();
    expect(role).toEqual(mockUser.role.roleName);
  });

  it('should return null for user role if no user is set', () => {
    (window.sessionStorage.getItem as jasmine.Spy).and.returnValue(null);
    const role = service.getUserRole();
    expect(role).toBeNull();
  });

  it('should retrieve user role ID', () => {
    const roleId = service.getUserRoleId();
    expect(roleId).toEqual(mockUser.role.id);
  });

  it('should return null for user role ID if no user is set', () => {
    (window.sessionStorage.getItem as jasmine.Spy).and.returnValue(null);
    const roleId = service.getUserRoleId();
    expect(roleId).toBeNull();
  });

  it('should check if user is authenticated', () => {
    const isAuthenticated = service.isAuthenticated();
    expect(isAuthenticated).toBeTrue();
  });

  it('should return false for isAuthenticated if no token is set', () => {
    (window.sessionStorage.getItem as jasmine.Spy).and.returnValue(null);
    const isAuthenticated = service.isAuthenticated();
    expect(isAuthenticated).toBeFalse();
  });

  it('should clear user data on logout', () => {
    service.logout();
    expect(window.sessionStorage.removeItem).toHaveBeenCalledWith('user_data');
  });


  it('should return null for userWithoutToken if no user is set', () => {
    (window.sessionStorage.getItem as jasmine.Spy).and.returnValue(null);
    const userWithoutToken = service.getUserWithoutToken();
    expect(userWithoutToken).toBeNull();
  });
});

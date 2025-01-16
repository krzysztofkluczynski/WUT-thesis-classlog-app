import {Injectable} from '@angular/core';
import {UserDto} from '../../model/entities/user-dto';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private user: UserDto | null = null;

  constructor() {
    const userData = window.sessionStorage.getItem('user_data');
    this.user = userData ? JSON.parse(userData) : null;
  }

  setUser(user: UserDto | null): void {
    this.user = user;
    if (user !== null) {
      window.sessionStorage.setItem('user_data', JSON.stringify(user));
    } else {
      window.sessionStorage.removeItem('user_data');
    }
  }

  getUser(): UserDto | null {
    const userData = window.sessionStorage.getItem('user_data');
    this.user = userData ? JSON.parse(userData) : null;
    return this.user;
  }

  getAuthToken(): string | null {
    const user = this.getUser();
    return user?.token || null;
  }

  getUserRole(): string | null {
    const user = this.getUser();
    return user?.role?.roleName || null;
  }

  getUserRoleId(): number | null {
    const user = this.getUser();
    return user?.role?.id || null;
  }

  isAuthenticated(): boolean {
    const token = this.getAuthToken();
    return !!token;
  }

  logout(): void {
    this.setUser(null)
  }

  getUserWithoutToken(): Omit<UserDto, 'token'> | null {
    const user = this.getUser();
    if (user) {
      const {token, ...userWithoutToken} = user;
      return userWithoutToken;
    }
    return null;
  }
}

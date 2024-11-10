import { Injectable } from '@angular/core';
import { UserDto } from '../../model/entities/user-dto'; // Import UserDto

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private user: UserDto | null = null;

  constructor() {}

  setUser(user: UserDto | null): void {
    this.user = user;

    if (user !== null) {
      window.localStorage.setItem("user_data", JSON.stringify(user));
      return;  // Explicit return for non-null case
    } else {
      window.localStorage.removeItem("user_data");
      return;  // Explicit return for null case
    }
  }

  getUser(): UserDto | null {
    if (this.user) {
      return this.user;
    }

    const userData = window.localStorage.getItem("user_data");

    if (userData) {
      return JSON.parse(userData);
    }

    return null;
  }

  getAuthToken(): string | null {
    const user = this.getUser();

    if (user && user.token) {
      return user.token;  // Return user token if available
    }

    return null;  // Return null if no token is available
  }

  getUserRole(): string | null {
    const user = this.getUser();

    if (user && user.role) {
      return user.role.roleName;  // Return user role name if available
    }

    return null;  // Return null if no role is available
  }

  getUserRoleId(): number | null {
    const user = this.getUser();

    if (user && user.role) {
      return user.role.id;  // Return user role name if available
    }

    return null;  // Return null if no role is available
  }

  isAuthenticated(): boolean {
    const token = this.getAuthToken();

    if (token) {
      return true;  // Return true if token exists
    }

    return false;  // Return false if no token exists
  }

  logout(): void {
    this.setUser(null);  // Clear user data
    return;  // Explicit return to indicate end of the function
  }
}

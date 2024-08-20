import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private token: string | null = null;
  private userRole: string | null = null;

  constructor() {}

  setAuthToken(token: string | null): void {
    this.token = token;
    if (token !== null) {
      window.localStorage.setItem("auth_token", token);
    } else {
      window.localStorage.removeItem("auth_token");
    }
  }

  getAuthToken(): string | null {
    return this.token || window.localStorage.getItem("auth_token");
  }

  setUserRole(role: string | null): void {
    this.userRole = role;
    if (typeof role === "string") {
      window.localStorage.setItem('user_role', role);
    } else {
      window.localStorage.removeItem("user_role");
    }
  }

  getUserRole(): string | null {
    return this.userRole || window.localStorage.getItem("user_role");
  }

  isAuthenticated(): boolean {
    return !!this.getAuthToken();
  }

  logout(): void {
    this.setAuthToken(null);
    this.setUserRole(null);
  }
}

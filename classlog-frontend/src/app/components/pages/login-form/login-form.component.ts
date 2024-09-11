import { Component } from '@angular/core';
import { FormsModule } from "@angular/forms";
import { NgClass } from "@angular/common";
import { Router } from '@angular/router';
import { UserDto } from "../../../model/user-dto.model";
import { AxiosService } from "../../../service/axios/axios.service";
import { AuthService } from "../../../service/auth/auth.service";
import { ErrorResponse } from "../../../model/error-response.model";
import {HeaderComponent} from "../../shared/header/header.component";

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    HeaderComponent
  ],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'] // Corrected from styleUrl to styleUrls
})
export class LoginFormComponent {
  login: string = "";
  password: string = "";

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmitLogin(): void {
    this.axiosService.request('POST', '/login', {
      login: this.login,
      password: this.password
    }).then((response: { data: UserDto }) => {
      const user: UserDto = response.data;
      console.log(user);

      // Store the entire user in AuthService
      this.authService.setUser(user);

      const userRole = this.authService.getUserRole();
      switch (userRole) {
        case 'admin':
          this.router.navigate(['/admin-dashboard']);
          break;
        case 'student':
          this.router.navigate(['/user-dashboard']);
          break;
        case 'teacher':
          this.router.navigate(['/teacher-dashboard']);
          break;
        default:
          console.error('Unknown role:', userRole);
          this.router.navigate(['/login']);
          break;
      }
    }).catch((error: ErrorResponse) => {
      console.error('Login error:', error.message);
      this.authService.setUser(null);  // Clear any stored user data on error
    });
  }

  navigateToRegister(event: Event): void {
    event.preventDefault();
    this.router.navigate(['/register']);
  }
}

import { Component } from '@angular/core';
import { Router } from '@angular/router'; // Import the Router service
import { HeaderComponent } from "../../shared/header/header.component";
import { LoginFormComponent } from "./login-form/login-form.component";
import { AxiosService } from "../../../service/axios/axios.service";
import { AuthService } from "../../../service/auth/auth.service";
import { UserDto } from "../../../model/user-dto.model";

interface ErrorResponse {
  message: string;
}

@Component({
  selector: 'app-starting-page',
  standalone: true,
  imports: [
    HeaderComponent,
    LoginFormComponent
  ],
  templateUrl: './starting-page.component.html',
  styleUrl: './starting-page.component.css'
})
export class StartingPageComponent {
  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin(input: any): void {
    this.axiosService.request('POST', '/login', {
      login: input.login,
      password: input.password
    }).then((response: { data: UserDto }) => {
      const user: UserDto = response.data;
      console.log(user)
      // Store token and role in AuthService
      this.authService.setAuthToken(user.token);
      this.authService.setUserRole(user.role.roleName);

      // Redirect based on role
      if (this.authService.getUserRole() === 'admin') {
        this.router.navigate(['/admin-dashboard']);
      } else if (this.authService.getUserRole() === 'student') {
        this.router.navigate(['/user-dashboard']);
      } else if (this.authService.getUserRole() === 'teacher') {
        this.router.navigate(['/teacher-dashboard']);
      } else {
        console.error('Unknown role:', user.role.roleName);
        this.router.navigate(['/login']);
      }
    }).catch(
      (error: ErrorResponse) => {
        this.authService.setAuthToken(null);
        // Optionally, handle error feedback here
      });
  }
}

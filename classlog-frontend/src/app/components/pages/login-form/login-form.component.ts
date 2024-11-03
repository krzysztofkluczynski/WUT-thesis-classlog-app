import { Component } from '@angular/core';
import { FormsModule } from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import { Router } from '@angular/router';
import { UserDto } from "../../../model/user-dto.model";
import { AxiosService } from "../../../service/axios/axios.service";
import { AuthService } from "../../../service/auth/auth.service";
import { ErrorResponse } from "../../../model/error-response.model";
import { HeaderComponent } from "../../shared/header/header.component";
import {ErrorDialogComponent} from "../../shared/error-dialog/error-dialog.component";

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    HeaderComponent,
    ErrorDialogComponent,
    NgIf,
    // Include ErrorDialogComponent in imports
  ],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {
  email: string = "";
  password: string = "";
  showErrorDialog: boolean = false;  // Control visibility of the ErrorDialog
  errorMessage: string = '';

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmitLogin(): void {
    this.axiosService.request('POST', '/login', {
      email: this.email,
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
        case 'unknown':
          this.router.navigate(['/unknown-dashboard']);
          break;
        default:
          console.error('Unknown role:', userRole);
          this.router.navigate(['/login']);
          break;
      }
    }).catch((error: any) => {
      let errorResponse: ErrorResponse;

      if (error.response) {
        const statusCode = error.response.status;
        const message = error.response.data.message || 'An error occurred';
        const details = error.response.data.details || null;

        errorResponse = new ErrorResponse(statusCode, message, details);

        this.errorMessage = errorResponse.getMessage();
        this.showErrorDialog = true;

        console.error(`Error details:`, errorResponse.getDetails());
      } else {
        // Handle errors without a response (e.g., network issues)
        errorResponse = new ErrorResponse(0, error.message);  // 0 as default for non-HTTP errors
        this.errorMessage = errorResponse.getMessage();
        this.showErrorDialog = true;  // Open the custom error dialog
      }

      // Clear any stored user data on error
      this.authService.setUser(null);
    });
  }

  navigateToRegister(event: Event): void {
    event.preventDefault();
    this.router.navigate(['/register']);
  }

  closeErrorDialog(): void {
    this.showErrorDialog = false;  // Method to close the error dialog
  }
}

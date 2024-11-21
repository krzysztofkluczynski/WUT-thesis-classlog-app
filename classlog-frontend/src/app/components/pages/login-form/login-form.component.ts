import {Component, OnInit} from '@angular/core';
import { FormsModule } from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import { Router } from '@angular/router';
import { UserDto } from "../../../model/entities/user-dto";
import { AxiosService } from "../../../service/axios/axios.service";
import { AuthService } from "../../../service/auth/auth.service";
import { HeaderComponent } from "../../shared/header/header.component";
import {GlobalNotificationHandler} from "../../../service/notification/global-notification-handler.service";

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    HeaderComponent,
    NgIf,
  ],
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit{
  email: string = "";
  password: string = "";

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {
    this.authService.logout()
  }


  onSubmitLogin(): void {
    this.axiosService.request('POST', '/login', {
      email: this.email,
      password: this.password
    }).then((response: { data: UserDto }) => {
      const user: UserDto = response.data;
      console.log(user);

      this.authService.setUser(user);

      const userRole = this.authService.getUserRole();
      switch (userRole) {
        case 'Admin':
          console.log('Admin');
          this.router.navigate(['/admin/students']);
          break;
        case 'Student':
          this.router.navigate(['/student/dashboard']);
          break;
        case 'Teacher':
          this.router.navigate(['/teacher/dashboard']);
          break;
        case 'Unknown':
          this.router.navigate(['/unknown']);
          break;
        default:
          console.error('Unknown role:', userRole);
          this.router.navigate(['/login']);
          break;
      }
    }).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      this.authService.setUser(null);
    });
  }

  navigateToRegister(event: Event): void {
    event.preventDefault();
    this.router.navigate(['/register']);
  }

}

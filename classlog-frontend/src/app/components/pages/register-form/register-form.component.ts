import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {Router} from '@angular/router';
import {HeaderComponent} from "../../shared/header/header.component";
import {UserDto} from "../../../model/entities/user-dto";
import {AxiosService} from "../../../service/axios/axios.service";
import {AuthService} from "../../../service/auth/auth.service";
import {GlobalNotificationHandler} from "../../../service/notification/global-notification-handler.service";

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    HeaderComponent,
    NgIf
  ],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {

  email: string = "";
  name: string = "";
  surname: string = "";
  password: string = "";
  confirmPassword: string = "";
  passwordMismatch: boolean = false;


  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
) {}

  onSubmitRegister(): void {
    if (this.passwordMismatch) {
      alert('Password do not match');
      return;
    }

    this.axiosService.request('POST', '/register', {
      name: this.name,
      surname: this.surname,
      email: this.email,
      password: this.password
    }).then((response: { data: UserDto }) => {
      const user: UserDto = response.data;
      this.globalNotificationHandler.handleMessage('Registration successful');

      this.axiosService.request('POST', '/login', {
        email: this.email,
        password: this.password
      }).then((response: { data: UserDto }) => {
        const user: UserDto = response.data;
        this.authService.setUser(user);
        this.router.navigate(['/unknown']);

    }).catch((error: any) => {
        this.globalNotificationHandler.handleError('Registration failed. Please try again.');
    });
  });
  }

  navigateToLogin(event: Event): void {
    event.preventDefault();
    this.router.navigate(['/login']);
  }

  checkPasswordMatch() {
    this.passwordMismatch = this.password !== this.confirmPassword;
  }
}

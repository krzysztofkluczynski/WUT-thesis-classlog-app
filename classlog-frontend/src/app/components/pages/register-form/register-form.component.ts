import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgClass, NgIf} from "@angular/common";
import {Router} from '@angular/router';
import {HeaderComponent} from "../../shared/header/header.component";
import {UserDto} from "../../../model/user-dto.model";
import {AxiosService} from "../../../service/axios/axios.service";
import {AuthService} from "../../../service/auth/auth.service";

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
    private router: Router
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
      console.log('Registration successful:', user);

      this.router.navigate(['/login']);
    }).catch((error: any) => {
      console.error('Registration failed:', error);
      alert('Registration failed. Please try again.');
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

import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgClass} from "@angular/common";
import {Router} from '@angular/router';
import {HeaderComponent} from "../../shared/header/header.component";

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [
    FormsModule,
    NgClass,
    HeaderComponent
  ],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent {

  username: string = "";
  password: string = "";
  confirmPassword: string = "";

  constructor(private router: Router) {}

  onSubmitRegister(): void {
    this.router.navigate(['/login']);
  }

  navigateToLogin(): void {
    this.router.navigate(['/login']);
  }
}

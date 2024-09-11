import { Component } from '@angular/core';
import {HeaderComponent} from "../../shared/header/header.component";
import {LoginFormComponent} from "../login-form/login-form.component";
import {UserDto} from "../../../model/user-dto.model";
import {AuthService} from "../../../service/auth/auth.service";
import {AxiosService} from "../../../service/axios/axios.service";

@Component({
  selector: 'app-admin-components',
  standalone: true,
  imports: [
    HeaderComponent,
    LoginFormComponent
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  user: UserDto | null = null;

  constructor(
    private authService: AuthService,  // Inject AuthService
    private axiosService: AxiosService // Inject AxiosService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUser(); // Get the current user from AuthService
  }

}

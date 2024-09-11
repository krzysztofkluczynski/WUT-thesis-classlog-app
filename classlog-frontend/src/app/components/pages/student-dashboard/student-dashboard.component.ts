import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../../shared/header/header.component";
import { AuthService } from "../../../service/auth/auth.service";
import { AxiosService } from "../../../service/axios/axios.service";
import { UserDto } from "../../../model/user-dto.model";

@Component({
  selector: 'app-student-components',
  standalone: true,
  imports: [
    HeaderComponent
  ],
  templateUrl: './student-dashboard.component.html',
  styleUrl: './student-dashboard.component.css'
})
export class StudentDashboardComponent implements OnInit {
  user: UserDto | null = null;

  constructor(
    private authService: AuthService,  // Inject AuthService
    private axiosService: AxiosService // Inject AxiosService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUser(); // Get the current user from AuthService
  }
}

import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../../../shared/header/header.component";
import { AuthService } from "../../../../service/auth/auth.service";
import { AxiosService } from "../../../../service/axios/axios.service";
import { UserDto } from "../../../../model/user-dto.model";
import {NgForOf} from "@angular/common";

interface UserResponse {
  data: string[];
}
@Component({
  selector: 'app-student-components',
  standalone: true,
  imports: [
    HeaderComponent,
    NgForOf
  ],
  templateUrl: './student-dashboard.component.html',
  styleUrl: './student-dashboard.component.css'
})


export class StudentDashboardComponent implements OnInit {
  user: UserDto | null = null;
  data: string[] = [];

  constructor(
    private authService: AuthService,  // Inject AuthService
    private axiosService: AxiosService // Inject AxiosService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUser(); // Get the current user from AuthService
    this.axiosService.request('GET', '/users', {}).then(
      (response: UserResponse) => this.data = response.data);
  }
}

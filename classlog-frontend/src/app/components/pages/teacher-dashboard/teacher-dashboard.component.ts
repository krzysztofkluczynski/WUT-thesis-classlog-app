import { Component } from '@angular/core';
import {HeaderComponent} from "../../shared/header/header.component";
import {UserDto} from "../../../model/user-dto.model";
import {AuthService} from "../../../service/auth/auth.service";
import {AxiosService} from "../../../service/axios/axios.service";

@Component({
  selector: 'app-teacher-components',
  standalone: true,
  imports: [
    HeaderComponent
  ],
  templateUrl: './teacher-dashboard.component.html',
  styleUrl: './teacher-dashboard.component.css'
})
export class TeacherDashboardComponent {
  user: UserDto | null = null;

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUser();
  }
}

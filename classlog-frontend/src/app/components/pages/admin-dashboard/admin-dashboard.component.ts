import {Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../../shared/header/header.component";
import {LoginFormComponent} from "../login-form/login-form.component";
import {UserDto} from "../../../model/user-dto.model";
import {AuthService} from "../../../service/auth/auth.service";
import {AxiosService} from "../../../service/axios/axios.service";
import {ClassTileComponent} from "../../shared/class-tile/class-tile.component";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-components',
  standalone: true,
    imports: [
        HeaderComponent,
        LoginFormComponent,
        ClassTileComponent,
      CommonModule
    ],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  user: UserDto | null = null;
  UsersList: UserDto[] = []

  constructor(
    private authService: AuthService,  // Inject AuthService
    private axiosService: AxiosService // Inject AxiosService
  ) {}


  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.axiosService.request('GET', '/users', {}).then(
      (response: { data: UserDto[] }) => {
        this.UsersList = response.data;
      }
    ).catch((error: any) => console.error('Failed to fetch users:', error));
  }

}

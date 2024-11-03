import {Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../../shared/header/header.component";
import {LoginFormComponent} from "../login-form/login-form.component";
import {UserDto} from "../../../model/user-dto.model";
import {AuthService} from "../../../service/auth/auth.service";
import {AxiosService} from "../../../service/axios/axios.service";
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { parseDate } from '../../../utils/date-utils';


@Component({
  selector: 'app-admin-components',
  standalone: true,
    imports: [
        HeaderComponent,
        LoginFormComponent,
      CommonModule
    ],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  user: UserDto | null = null;
  UsersList: UserDto[] = []
  pickedUser: string = 'Students';

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private router: Router
  ) {}

  goToUserProfile(userId: number): void {
    this.router.navigate(['/user-profile', userId], { queryParams: { editMode: true } });
  }

  loadUsers(userId: number): void {
    if (userId === 1) {
      this.pickedUser = 'Teachers';
    } else if (userId === 2) {
      this.pickedUser = 'Students';
    } else if (userId === 3) {
      this.pickedUser = 'Admins';
    } else if (userId === 4) {
      this.pickedUser = 'Unassigned';
    }
    this.axiosService.request('GET', `/users/role/${userId}`, {}).then(
      (response: { data: UserDto[] }) => {
        this.UsersList = response.data.map(user => ({
          ...user,
          createdAt: parseDate(user.createdAt)  // Ensure createdAt is a Date
        }));
      }
    ).catch((error: any) => console.error('Failed to fetch users:', error));
  }

  onOptionSelected(option: string): void {
      if (option === 'Teachers') {
        this.loadUsers(1);
      } else if (option === 'Students') {
        this.loadUsers(2);
      } else if (option === 'Admins') {
        this.loadUsers(3);
      } else if (option === 'Unassigned') {
        this.loadUsers(4);
      }
  }

  deleteUser(userId: number): void {
    this.axiosService.request('DELETE', `/users/${userId}`, {}).then(
      (response: any) => {
        this.UsersList = this.UsersList.filter(user => user.id !== userId);
        console.log(`User with ID ${userId} deleted successfully. Response:`, response);
      }
    ).catch((error: any) => console.error('Failed to delete user:', error));
  }

  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.loadUsers(2)
  }

}

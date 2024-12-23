import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HeaderComponent } from "../../../shared/header/header.component";
import { LoginFormComponent } from "../../login-form/login-form.component";
import { UserDto } from "../../../../model/entities/user-dto";
import { AuthService } from "../../../../service/auth/auth.service";
import { AxiosService } from "../../../../service/axios/axios.service";
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { parseDate } from '../../../../utils/date-utils';
import { GlobalNotificationHandler } from "../../../../service/notification/global-notification-handler.service";

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
  usersList: UserDto[] = [];
  pickedRole: string | null = null;

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.user = this.authService.getUser();
    this.route.paramMap.subscribe(params => {
      const section = params.get('section');
      switch (section) {
        case 'teachers':
          this.loadUsers(1);
          this.pickedRole = 'Teacher';
          break;
        case 'students':
          this.loadUsers(2);
          this.pickedRole = 'Student';
          break;
        case 'admins':
          this.loadUsers(3);
          this.pickedRole = 'Admin';
          break;
        case 'unassigned':
          this.loadUsers(4);
          this.pickedRole = 'Unassigned';
          break;
        default:
          break;
      }
    });
  }

  goToUserProfile(userId: number): void {
    this.router.navigate(['/profile', userId], { queryParams: { editMode: true } });
  }

  loadUsers(userId: number): void {
    this.axiosService.request('GET', `/users/role/${userId}`, {}).then(
      (response: { data: UserDto[] }) => {
        this.usersList = response.data.map(user => ({
          ...user,
          createdAt: parseDate(user.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
    });
  }



  deleteUser(userId: number): void {
    this.axiosService.request('DELETE', `/users/${userId}`, {}).then(
      (response: any) => {
        this.usersList = this.usersList.filter(user => user.id !== userId);
        this.globalNotificationHandler.handleMessagewithType(`User with ID ${userId} deleted successfully.`, 'success');
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
    });  }

}

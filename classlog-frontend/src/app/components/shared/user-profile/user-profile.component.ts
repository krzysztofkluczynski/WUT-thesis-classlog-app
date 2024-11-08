import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { DatePipe, NgIf } from "@angular/common";
import { UserDto } from "../../../model/user-dto.model";
import { AuthService } from "../../../service/auth/auth.service";
import { AxiosService } from "../../../service/axios/axios.service";
import { parseDate } from "../../../utils/date-utils";
import { HeaderComponent } from "../header/header.component";
import { FormsModule } from '@angular/forms';
import {GlobalErrorHandler} from "../../../service/error/global-error-handler.service";
import {Role} from "../../../model/role.model";
import {ChangePasswordDto} from "../../../model/change-password-dto";

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    NgIf,
    HeaderComponent,
    DatePipe,
    FormsModule
  ],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  userId!: number;
  editMode = false;
  editButtonClicked = false;
  changePasswordClicked = false;
  currentPassword = '';
  newPassword = '';
  confirmNewPassword = '';
  userDto: UserDto = {
    id: 0,
    name: '',
    surname: '',
    email: '',
    role: { id: 0, roleName: 'Unknown' },
    createdAt: new Date(),
    token: '',
  };

  constructor(
    private route: ActivatedRoute,
    public authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private globalErrorHandler: GlobalErrorHandler
  ) {}

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id')!;

    this.route.queryParams.subscribe(params => {
      this.editMode = params['editMode'] === 'true';
    });

    this.axiosService.request('GET', `/users/${this.userId}`, {})
      .then((response: { data: UserDto }) => {
        this.userDto = {
          ...response.data,
          createdAt: parseDate(response.data.createdAt),
        };
      })
      .catch((error: any) => {
        this.globalErrorHandler.handleError(error);
        console.error('Failed to fetch user data:', error);
      });
  }

  editUser(): void {
    this.editButtonClicked = true;
  }

  saveChanges(): void {
    const roleMapping: { [key: string]: Role } = {
      Teacher: { id: 1, roleName: 'Teacher' },
      Student: { id: 2, roleName: 'Student' },
      Admin: { id: 3, roleName: 'Admin' },
      Unknown: { id: 4, roleName: 'Unknown' },
    };

    const selectedRole = roleMapping[this.userDto.role.roleName];

    const updatedUser: UserDto = {
      ...this.userDto,
      role: selectedRole,
    };

    if (this.changePasswordClicked && this.currentPassword !== '' && this.newPassword !== '' && this.confirmNewPassword !== '') {
      if (this.newPassword !== this.confirmNewPassword) {
        this.globalErrorHandler.handleError('Passwords do not match.');
        this.cancelChanges()
        return;
      }

      const changePasswordDto: ChangePasswordDto = {
        userId: this.userDto.id,
        oldPassword: this.currentPassword,
        newPassword: this.newPassword,
      }

      this.axiosService.request('POST', `/users/change-password`, changePasswordDto)
        .then((response: any) => {
          console.log(`User with ID ${this.userId} changed password. Response:`, response);
        })
        .catch((error: any) => {
          this.globalErrorHandler.handleError(error);
        });
    }

    this.axiosService.request('PUT', `/users/${this.userId}`, updatedUser)
      .then((response: any) => {
        console.log(`User with ID ${this.userId} updated successfully. Response:`, response);
        this.editButtonClicked = false;
        this.changePasswordClicked = false;
        this.ngOnInit();
      })
      .catch((error: any) => {
        this.globalErrorHandler.handleError(error);
        console.error('Failed to update user data:', error);
        this.ngOnInit();
      });
  }

  deleteUser() {
    this.axiosService.request('DELETE', `/users/${this.userId}`, {}).then(
      (response: any) => {
        console.log(`User with ID ${this.userId} deleted successfully. Response:`, response);
      }
    ).catch((error: any) => {
      this.globalErrorHandler.handleError(error);
      console.error('Failed to delete user:', error);
    });
  }

  cancelChanges() {
    this.editButtonClicked = false;
    this.changePasswordClicked = false;
    this.currentPassword = '';
    this.newPassword = '';
    this.confirmNewPassword = '';
    this.ngOnInit();
  }

  toggleChangePassword() {
    this.changePasswordClicked = !this.changePasswordClicked;
  }
}

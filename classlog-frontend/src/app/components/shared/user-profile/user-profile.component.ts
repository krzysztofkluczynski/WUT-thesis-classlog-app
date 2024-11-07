import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {DatePipe, NgIf} from "@angular/common";
import {UserDto} from "../../../model/user-dto.model";
import {AuthService} from "../../../service/auth/auth.service";
import {AxiosService} from "../../../service/axios/axios.service";
import {parseDate} from "../../../utils/date-utils";
import {HeaderComponent} from "../header/header.component";
import {FormsModule} from '@angular/forms';
import {Role} from "../../../model/role.model";

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
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  userId!: number;
  editMode = false;
  editButtonClicked = false;
  userDto: UserDto = {
    id: 0,
    name: '',
    surname: '',
    email: '',
    role: { id: 0, roleName: 'Unknown' }, // Matches Role interface
    createdAt: new Date(),
    token: '',
  };


  constructor(private route: ActivatedRoute,
              private authService: AuthService,
              private axiosService: AxiosService,
              private router: Router) {}

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id')!;

    this.route.queryParams.subscribe(params => {
      this.editMode = params['editMode'] === 'true';
    });

    this.axiosService.request('GET', `/users/${this.userId}`, {})
      .then((response: { data: UserDto }) => {
        this.userDto = {
          ...response.data,
          createdAt: parseDate(response.data.createdAt), // Ensure createdAt is a Date
        };
      })
      .catch((error: any) => console.error('Failed to fetch user data:', error));
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

    const selectedRole = roleMapping[this.userDto.role.roleName] || { id: 4, roleName: 'Unknown' };

    const updatedUser: UserDto = {
      ...this.userDto,
      role: selectedRole,
    };

    this.axiosService.request('PUT', `/users/${this.userId}`, updatedUser)
      .then((response: any) => {
        console.log(`User with ID ${this.userId} updated successfully. Response:`, response);
        this.editButtonClicked = false;
        this.ngOnInit();
      })
      .catch((error: any) => {
        console.error('Failed to update user data:', error);
        this.ngOnInit();
      });
  }

  deleteUser() {
    this.axiosService.request('DELETE', `/users/${this.userId}`, {}).then(
      (response: any) => {
        console.log(`User with ID ${this.userId} deleted successfully. Response:`, response);
      }
    ).catch((error: any) => console.error('Failed to delete user:', error));
  }

  cancelChanges() {
    this.editButtonClicked = false;
    this.ngOnInit(); //
  }
}

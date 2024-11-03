import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {NgIf} from "@angular/common";
import {UserDto} from "../../../model/user-dto.model";
import {AuthService} from "../../../service/auth/auth.service";
import {AxiosService} from "../../../service/axios/axios.service";
import {parseDate} from "../../../utils/date-utils";

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  userId!: number;
  editMode = false;
  userDto: UserDto | null = null;

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
        this.userDto = response.data
      })
      .catch((error: any) => console.error('Failed to fetch user data:', error));
  }


  editUser(): void {
    console.log('Edit mode active for user:', this.userId);
    // Add further edit functionality here
  }
}

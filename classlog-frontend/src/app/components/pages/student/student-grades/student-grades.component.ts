import {Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {UserDto} from "../../../../model/entities/user-dto";
import {parseDate} from "../../../../utils/date-utils";
import {AuthService} from "../../../../service/auth/auth.service";
import {AxiosService} from "../../../../service/axios/axios.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {GradeDto} from "../../../../model/entities/grade-dto";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";
import {getFullName} from "../../../../utils/user-utils";

@Component({
  selector: 'app-student-grades',
  standalone: true,
  imports: [
    HeaderComponent,
    DatePipe,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './student-grades.component.html',
  styleUrl: './student-grades.component.css'
})
export class StudentGradesComponent implements OnInit {

  studentId: number = 0;
  grades: GradeDto[] = [];
  userDto: UserDto = {
    id: 0,
    name: '',
    surname: '',
    email: '',
    role: {id: 4, roleName: 'Unknown' },
    token: '', // Empty token
    createdAt: new Date(),
  };
  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
  private globalNotificationHandler: GlobalNotificationHandler,
  ) {
  }


  ngOnInit(): void {
    this.activatedRoute.params.subscribe(params => {
      this.studentId = params['studentId'];

    this.axiosService.request('GET', `/users/${this.studentId}`, {})
      .then((response: { data: UserDto }) => {
        this.userDto = {
          ...response.data,
          createdAt: parseDate(response.data.createdAt),
        };
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch user data:', error);
      });


    this.axiosService.request('GET', `/grades/user/${this.studentId}`, {}).then(
      (response: { data: GradeDto[] }) => {
        this.grades = response.data.map(grade => ({
          ...grade,
          createdAt: parseDate(grade.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch grades:', error);
    });
  });
  }

  protected readonly getFullName = getFullName;
}

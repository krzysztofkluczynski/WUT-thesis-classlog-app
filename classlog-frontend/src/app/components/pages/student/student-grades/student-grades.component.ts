import {Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {UserDto} from "../../../../model/entities/user-dto";
import {parseDate} from "../../../../utils/date-utils";
import {AuthService} from "../../../../service/auth/auth.service";
import {AxiosService} from "../../../../service/axios/axios.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {GradeDto} from "../../../../model/entities/grade-dto";
import {DatePipe, NgForOf} from "@angular/common";

@Component({
  selector: 'app-student-grades',
  standalone: true,
  imports: [
    HeaderComponent,
    DatePipe,
    NgForOf
  ],
  templateUrl: './student-grades.component.html',
  styleUrl: './student-grades.component.css'
})
export class StudentGradesComponent implements OnInit {

  gradesList: GradeDto[] = [];

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
  ) {}


  ngOnInit(): void {
    this.axiosService.request('GET', `/grades/user/${this.authService.getUser()?.id}`, {}).then(
      (response: { data: GradeDto[] }) => {
        this.gradesList = response.data.map(grade => ({
          ...grade,
          createdAt: parseDate(grade.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed detch grades:', error);
    });
  }

}

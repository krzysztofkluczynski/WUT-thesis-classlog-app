import {Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {ClassDto} from "../../../../model/entities/class-dto";
import {UserDto} from "../../../../model/entities/user-dto";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {parseDate} from "../../../../utils/date-utils";
import {ClassTileComponent} from "../../../shared/class-tile/class-tile.component";
import {DatePipe, NgForOf} from "@angular/common";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-teacher-grades',
  standalone: true,
  imports: [
    HeaderComponent,
    ClassTileComponent,
    DatePipe,
    NgForOf,
    FormsModule
  ],
  templateUrl: './teacher-grades.component.html',
  styleUrl: './teacher-grades.component.css'
})
export class TeacherGradesComponent implements OnInit {
  classList: ClassDto[] = [];
  studentList: UserDto[] = [];
  selectedClassId: string | null = null;

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {
    this.axiosService.request('GET', `/classes/user/${this.authService.getUser()?.id}`, {}).then(
      (response: { data: ClassDto[] }) => {
        this.classList = response.data.map(classDto => ({
          ...classDto,
          createdAt: parseDate(classDto.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch classes:', error);
    });
  }

  loadStudents(): void {
    if (this.selectedClassId) {
      this.axiosService.request('GET', `/users/class/${this.selectedClassId}/role/${2}`, {}).then(
        (response: { data: UserDto[] }) => {
          this.studentList = response.data.map(student => ({
            ...student,
            createdAt: parseDate(student.createdAt)
          }));
        }
      ).catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch students:', error);
      });
    }
  }

  addNewGrade() {

  }
}

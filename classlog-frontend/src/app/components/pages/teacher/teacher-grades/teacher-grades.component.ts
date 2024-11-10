import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {ClassDto} from "../../../../model/entities/class-dto";
import {UserDto} from "../../../../model/entities/user-dto";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {parseDate} from "../../../../utils/date-utils";
import {ClassTileComponent} from "../../../shared/class-tile/class-tile.component";
import {DatePipe, NgForOf, FormsModule} from "@angular/common";

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
export class TeacherGradesComponent {
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
      this.axiosService.request('GET', `/students/class/${this.selectedClassId}`, {}).then(
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
}

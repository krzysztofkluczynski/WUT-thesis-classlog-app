import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../../../shared/header/header.component";
import { ClassDto } from "../../../../model/entities/class-dto";
import { UserDto } from "../../../../model/entities/user-dto";
import { AxiosService } from "../../../../service/axios/axios.service";
import { AuthService } from "../../../../service/auth/auth.service";
import { Router } from "@angular/router";
import { GlobalNotificationHandler } from "../../../../service/notification/global-notification-handler.service";
import { parseDate } from "../../../../utils/date-utils";
import { ClassTileComponent } from "../../../shared/class-tile/class-tile.component";
import { DatePipe, NgForOf } from "@angular/common";
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
  styleUrls: ['./teacher-grades.component.css']
})
export class TeacherGradesComponent implements OnInit {
  classList: ClassDto[] = [];
  studentListFromOneClass: UserDto[] = [];
  allStudents: UserDto[] = [];
  filteredStudentList: UserDto[] = [];
  selectedClassId: string | null = null;
  searchQuery: string = '';

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {
    // Fetch classes for the current teacher
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

    // Fetch all students
    this.axiosService.request('GET', `/users/role/${2}`, {}).then(
      (response: { data: UserDto[] }) => {
        this.allStudents = response.data.map(user => ({
          ...user,
          createdAt: parseDate(user.createdAt)
        }));
        this.filteredStudentList = this.allStudents; // Initially show all students
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch all users:', error);
    });
  }

  // Load students based on selected class
  loadStudents(): void {
    if (this.selectedClassId) {
      this.axiosService.request('GET', `/users/class/${this.selectedClassId}/role/${2}`, {}).then(
        (response: { data: UserDto[] }) => {
          this.studentListFromOneClass = response.data.map(student => ({
            ...student,
            createdAt: parseDate(student.createdAt)
          }));
          this.filterStudents(); // Filter the list after fetching
        }
      ).catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch students:', error);
      });
    }
  }

  // Filter students based on search query
  filterStudents(): void {
    const searchQueryLower = this.searchQuery.toLowerCase();

      this.filteredStudentList = this.allStudents.filter(student =>
        `${student.name} ${student.surname}`.toLowerCase().includes(searchQueryLower)
      );
    }

  addNewGrade() {
    console.log('Add New Grade button clicked!');
    // Implement functionality here
  }

  onStudentClick(student: UserDto) {
    this.router.navigate(['/teacher/grades', student.id]);
  }
}

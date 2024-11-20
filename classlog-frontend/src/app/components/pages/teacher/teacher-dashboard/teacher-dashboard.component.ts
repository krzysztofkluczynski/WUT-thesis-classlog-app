import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../../../shared/header/header.component";
import { UserDto } from "../../../../model/entities/user-dto";
import { AuthService } from "../../../../service/auth/auth.service";
import { AxiosService } from "../../../../service/axios/axios.service";
import { ClassTileComponent } from "../../../shared/class-tile/class-tile.component";
import { parseDate } from "../../../../utils/date-utils";
import { ClassDto } from "../../../../model/entities/class-dto";
import { LessonDto } from "../../../../model/entities/lesson.dto";
import { GlobalNotificationHandler } from "../../../../service/notification/global-notification-handler.service";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {Router, withNavigationErrorHandler} from "@angular/router";
import {
    AddUserToClassWindowComponent
} from "../popup-window/add-user-to-class-window/add-user-to-class-window.component";
import {CreateClassWindowComponent} from "../popup-window/create-class-window/create-class-window.component";
import {JoinClassWindowComponent} from "../../../shared/popup/join-class-window/join-class-window.component";

@Component({
  selector: 'app-teacher-components',
  standalone: true,
  imports: [
    HeaderComponent,
    ClassTileComponent,
    NgForOf,
    DatePipe,
    AddUserToClassWindowComponent,
    NgIf,
    CreateClassWindowComponent,
    JoinClassWindowComponent
  ],
  templateUrl: './teacher-dashboard.component.html',
  styleUrls: ['./teacher-dashboard.component.css']
})
export class TeacherDashboardComponent implements OnInit {

  classList: ClassDto[] = [];
  lessons: LessonDto[] = [];
  numberOfLessonsToLoad: number = 4;
  showCreateClassModal: boolean = false;
  showJoinClassModal: boolean = false;

  teachersMap: Map<ClassDto, UserDto[]> = new Map();
  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {
    this.fetchData();
  }

  getTeachersForClass(classItem: ClassDto) {
    const teachers = this.teachersMap.get(classItem) || [];
    return teachers;
  }

  onClassTileClick(classItem: ClassDto) {
    this.router.navigate(['/teacher/class', classItem.id]);
  }

  toggleCreateClassModal() {
    this.showCreateClassModal = !this.showCreateClassModal;
  }

  toggleJoinClassModal() {
    this.showJoinClassModal = !this.showJoinClassModal;
    if (!this.showJoinClassModal) {
      this.fetchData();
      window.location.reload(); //TODO, Think of the better way to refresh the page
    }
  }

  private fetchData() {
    // Fetch classes
    this.axiosService.request('GET', `/classes/user/${this.authService.getUser()?.id}`, {}).then(
      (response: { data: ClassDto[] }) => {
        this.classList = response.data.map(classDto => ({
          ...classDto,
          createdAt: parseDate(classDto.createdAt)
        }));

        // Fetch users for each class with roleId = 1
        this.classList.forEach(classDto => {
          this.axiosService.request('GET', `/users/class/${classDto.id}/role/1`, {}).then(
            (userResponse: { data: UserDto[] }) => {
              const users = userResponse.data.map(user => ({
                ...user,
                createdAt: parseDate(user.createdAt)
              }));
              this.teachersMap.set(classDto, users);
            }
          ).catch((error: any) => {
            this.globalNotificationHandler.handleError(error);
            console.error(`Failed to fetch users for class ${classDto.id}:`, error);
          });
        });
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch classes:', error);
    });

    console.log('Before fetching lessons');
    this.axiosService.request('GET', `/lessons/user/${this.authService.getUser()?.id}/recent/${this.numberOfLessonsToLoad}`, {}).then(
      (response: { data: LessonDto[] }) => {
        console.log(response.data);
        this.lessons = response.data.map(lesson => ({
          ...lesson,
          lessonDate: parseDate(lesson.lessonDate)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch lessons:', error);
    });
  }

  onClassCreated(createdClass: ClassDto) {
    this.classList.push(createdClass);
    this.fetchData();
  }
}

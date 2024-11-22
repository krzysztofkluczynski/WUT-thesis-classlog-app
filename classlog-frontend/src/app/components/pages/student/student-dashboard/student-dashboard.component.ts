import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { HeaderComponent } from "../../../shared/header/header.component";
import { AuthService } from "../../../../service/auth/auth.service";
import { AxiosService } from "../../../../service/axios/axios.service";
import { UserDto } from "../../../../model/entities/user-dto";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {LessonDto} from "../../../../model/entities/lesson.dto";
import {parseDate} from "../../../../utils/date-utils";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {ClassTileComponent} from "../../../shared/class-tile/class-tile.component";
import {ClassDto} from "../../../../model/entities/class-dto";
import {Router} from "@angular/router";
import {JoinClassWindowComponent} from "../../../shared/popup/join-class-window/join-class-window.component";
import {LessonInfoWindowComponent} from "../../../shared/lesson-info-window/lesson-info-window.component";

interface UserResponse {
  data: string[];
}
@Component({
  selector: 'app-student-components',
  standalone: true,
    imports: [
        HeaderComponent,
        NgForOf,
        ClassTileComponent,
        DatePipe,
        JoinClassWindowComponent,
        NgIf,
        LessonInfoWindowComponent
    ],
  templateUrl: './student-dashboard.component.html',
  styleUrl: './student-dashboard.component.css'
})


export class StudentDashboardComponent implements OnInit {

  classList: ClassDto[] = [];
  lessons: LessonDto[] = [];
  numberOfLessonsToLoad: number = 4;

  teachersMap: Map<ClassDto, UserDto[]> = new Map();
  showJoinClassModal: boolean = false;
  showLessonModal: boolean = false;
  selectedLessonId: number | null = null;
  classIdForSelectedLesson: number | null = null;


  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
) {}

  ngOnInit(): void {
    this.fetchData();
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

  getTeachersForClass(classItem: ClassDto) {
    const teachers = this.teachersMap.get(classItem) || [];
    return teachers;
  }

  onClassTileClick(classItem: ClassDto) {
    this.router.navigate(['/student/class', classItem.id]);
  }

  toggleJoinClassModal() {
    this.showJoinClassModal = !this.showJoinClassModal;
    if (!this.showJoinClassModal) {
      this.fetchData();
      window.location.reload();
    }
  }

  toggleLessonWindow(lessonId: number | null, classId: number | null) {
    this.selectedLessonId = lessonId;
    this.classIdForSelectedLesson = classId;
    this.showLessonModal = !this.showLessonModal;
  }
}

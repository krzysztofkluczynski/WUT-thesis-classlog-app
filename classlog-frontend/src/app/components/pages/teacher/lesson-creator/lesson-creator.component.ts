import {Component, OnInit} from '@angular/core';
import {CommonModule, NgForOf} from '@angular/common';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { HeaderComponent } from "../../../shared/header/header.component";
import { AxiosService } from "../../../../service/axios/axios.service";
import { AuthService } from "../../../../service/auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import { GlobalNotificationHandler } from "../../../../service/notification/global-notification-handler.service";
import { UserDto } from "../../../../model/entities/user-dto";
import {parseDate} from "../../../../utils/date-utils";
import {consumerMarkDirty} from "@angular/core/primitives/signals";
import {ClassDto} from "../../../../model/entities/class-dto";
import {LessonDto} from "../../../../model/entities/lesson.dto";

export interface UserDtoWithPresence extends UserDto {
  isPresent: boolean; // Field to track attendance
}

@Component({
  selector: 'app-lesson-creator',
  standalone: true,
  imports: [
    HeaderComponent,
    FormsModule,
    NgForOf,
    // Include FormsModule
  ],
  templateUrl: './lesson-creator.component.html',
  styleUrls: ['./lesson-creator.component.css']
})
export class LessonCreatorComponent implements OnInit {
  studentListFromOneClass: UserDtoWithPresence[] = [];
  filteredStudentList: UserDtoWithPresence[] = [];
  classId: number | null = null;
  lessonDate: Date = new Date();
  lessonTime: string = '';
  lessonSubject: string = '';
  lessonNotes: string = '';
  searchQuery: string = '';
  createdLesson: LessonDto | null = null;
  presentStudents: UserDto[] = [];


  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void { //jesli wschodzimy z truby edycji musimy wczytac na poiczatek liste uczniow z calej klasy i oddzielnie atrybut isPresent im dodac
    this.classId = Number(this.route.snapshot.paramMap.get('classId'));

    this.axiosService.request('GET', `/users/class/${this.classId}/role/${2}`, {}).then(
      (response: { data: UserDto[] }) => {
        this.studentListFromOneClass = response.data.map(student => ({
          ...student,
          createdAt: parseDate(student.createdAt),
          isPresent: false
        }));
        this.filterStudents();
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch students:', error);
    });
  }

  filterStudents(): void {
    const searchQueryLower = this.searchQuery.toLowerCase();

    this.filteredStudentList = this.studentListFromOneClass.filter(student =>
      `${student.name} ${student.surname}`.toLowerCase().includes(searchQueryLower)
    );
  }

  createLesson() {
    // Combine date and time into a single Date object
    const [hours, minutes] = this.lessonTime.split(':').map(Number); // Extract hours and minutes
    const date = new Date(this.lessonDate); // Initialize date from lessonDate
    date.setHours(hours, minutes, 0, 0); // Set hours, minutes, and seconds

    const formattedDateTime = date.toISOString().slice(0, -1); // '2024-10-31T14:30:00'


    const lessonPayload = {
      createdByUser: this.authService.getUserWithoutToken(),
      lessonClass: {
        id: this.classId
      },
      lessonDate: formattedDateTime,
      subject: this.lessonSubject,
      content: this.lessonNotes
    }

    this.axiosService.request('POST', '/lessons', lessonPayload)
      .then((response: { data: LessonDto }) => {
        this.createdLesson = response.data;
        console.log('Lesson created:', this.createdLesson);

        this.globalNotificationHandler.handleMessage("Lesson created successfully");

        this.presentStudents = this.studentListFromOneClass
          .filter(student => student.isPresent)
          .map(({ isPresent, ...rest }) => rest);

        if (!this.createdLesson?.lessonId) {
          console.error('Lesson ID is missing or invalid:', this.createdLesson);
          return;
        }
        console.log(this.createdLesson)

        this.axiosService.request('POST', '/presence/add/students', {
          lessonId: this.createdLesson?.lessonId,
          users: this.presentStudents,
        }).then((response: { data: string }) => {
          this.globalNotificationHandler.handleMessage(response.data);
        }).catch((error: any) => {
          console.error('Failed to add users to class');
          this.globalNotificationHandler.handleError('Failed to add users to class. Please try again.');
        });


      })
      .catch((error: any) => {
        console.error('Failed to create class:', error);
        this.globalNotificationHandler.handleError(error);
      });
  }

  returnToDashboard() {
    this.router.navigate(['/teacher/dashboard']);
  }

  toggleCheckAll() {
    const isChecked = (document.getElementById('checkAll') as HTMLInputElement).checked;
    this.filteredStudentList.forEach(student => {
      student.isPresent = isChecked;
    });
  }

  toggleStudentPresence(student: UserDtoWithPresence) {
    student.isPresent = !student.isPresent;
  }
}

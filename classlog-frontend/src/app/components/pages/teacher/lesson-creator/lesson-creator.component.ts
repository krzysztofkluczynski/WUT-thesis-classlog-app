import {Component, OnInit} from '@angular/core';
import {NgForOf} from '@angular/common';
import {FormsModule} from '@angular/forms'; // Import FormsModule
import {HeaderComponent} from "../../../shared/header/header.component";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {UserDto} from "../../../../model/entities/user-dto";
import {parseDate} from "../../../../utils/date-utils";
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
  lessonId: number | null = null;
  lessonDate: string = '';
  lessonTime: string = '';
  lessonSubject: string = '';
  lessonNotes: string = '';
  searchQuery: string = '';
  createdLesson: LessonDto | null = null;
  presentStudents: UserDto[] = [];
  editMode: boolean = false;
  editModeLessonDto: LessonDto | null = null;
  editModePresentStudents: UserDto[] | null = null;


  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {
  }

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('classId'));
    const lessonId: number = Number(this.route.snapshot.queryParams['lessonId']);

    this.editMode = this.route.snapshot.queryParams['editMode'] === 'true';

    // Fetch all students in the class
    this.axiosService.request('GET', `/users/class/${this.classId}/role/2`, {}).then(
      (response: { data: UserDto[] }) => {
        this.studentListFromOneClass = response.data.map(student => ({
          ...student,
          createdAt: parseDate(student.createdAt),
          isPresent: false,
        }));
        this.filterStudents();

        if (this.editMode) {
          this.axiosService.request('GET', `/lessons/${lessonId}`, {}).then(
            (lessonResponse: { data: LessonDto }) => {
              this.editModeLessonDto = {
                ...lessonResponse.data,
                lessonDate: parseDate(lessonResponse.data.lessonDate),
              };


              const fullDate = new Date(this.editModeLessonDto.lessonDate || '');
              this.lessonDate = fullDate.toISOString().slice(0, 10);
              this.lessonTime = fullDate.toTimeString().slice(0, 5);
              this.lessonSubject = this.editModeLessonDto.subject || '';
              this.lessonNotes = this.editModeLessonDto.content || '';

              this.axiosService.request('GET', `/presence/students/${this.editModeLessonDto.lessonId}`, {}).then(
                (presenceResponse: { data: UserDto[] }) => {
                  this.editModePresentStudents = presenceResponse.data.map(student => ({
                    ...student,
                    createdAt: parseDate(student.createdAt),
                  }));

                  // Update `isPresent` for matching students
                  this.studentListFromOneClass.forEach(student => {
                    if (this.editModePresentStudents!.some(presentStudent => presentStudent.id === student.id)) {
                      student.isPresent = true;
                    }
                  });

                  this.filterStudents(); // Refresh filtered list
                }
              ).catch((error: any) => {
                this.globalNotificationHandler.handleError(error);
                console.error('Failed to fetch present students:', error);
              });
            }
          ).catch((error: any) => {
            this.globalNotificationHandler.handleError(error);
            console.error('Failed to fetch lesson:', error);
          });
        }
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
    if (this.lessonSubject === '') {
      this.globalNotificationHandler.handleMessage('Please enter a subject for the lesson.');
      return;
    }

    const [hours, minutes] = this.lessonTime.split(':').map(Number);
    const date = new Date(this.lessonDate);
    date.setHours(hours + 1, minutes, 0, 0);

    const formattedDateTime = date.toISOString().slice(0, -1);


    if (!this.editMode) {
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

          this.globalNotificationHandler.handleMessage("Lesson created successfully");

          this.presentStudents = this.studentListFromOneClass
            .filter(student => student.isPresent)
            .map(({isPresent, ...rest}) => rest);

          if (!this.createdLesson?.lessonId) {
            return;
          }

          this.axiosService.request('POST', '/presence/add/students', {
            lessonId: this.createdLesson?.lessonId,
            users: this.presentStudents,
          }).then((response: { data: string }) => {
            this.globalNotificationHandler.handleMessage(response.data);
          }).catch((error: any) => {
            this.globalNotificationHandler.handleError('Failed to add users to class. Please try again.');
          });
        })
        .catch((error: any) => {
          this.globalNotificationHandler.handleError(error);
        })
        .finally(() => {
          this.router.navigate(['/teacher/class/' + this.classId]);
        });
    } else {

      const lessonPayload = {
        lessonId: this.editModeLessonDto?.lessonId,
        createdByUser: this.editModeLessonDto?.createdByUser,
        lessonClass: this.editModeLessonDto?.lessonClass,
        lessonDate: formattedDateTime,
        subject: this.lessonSubject,
        content: this.lessonNotes
      }

      this.axiosService.request('PUT', '/lessons/update', lessonPayload)
        .then((response: { data: string }) => {
          this.globalNotificationHandler.handleMessage(response.data);


          this.axiosService.request('PUT', `/presence/update/${this.editModeLessonDto?.lessonId}`,
            this.studentListFromOneClass
              .filter(student => student.isPresent)
              .map(student => student.id)
          ).then((response: { data: string }) => {
            this.globalNotificationHandler.handleMessage(response.data);
          }).catch((error: any) => {
            this.globalNotificationHandler.handleError(error);
          });
        }).catch((error: any) => {
        console.error('Failed to update lesson');
        this.globalNotificationHandler.handleError(error);
      })
        .finally(() => {
          this.router.navigate(['/teacher/class/' + this.classId]);
        });
    }
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

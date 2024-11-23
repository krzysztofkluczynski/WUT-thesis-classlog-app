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
  ) {}

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('classId'));
    const lessonId: number = Number(this.route.snapshot.queryParams['lessonId']);

    this.editMode = this.route.snapshot.queryParams['editMode'] === 'true';
    console.log('Edit mode: ' + this.editMode);

    // Fetch all students in the class
    this.axiosService.request('GET', `/users/class/${this.classId}/role/2`, {}).then(
      (response: { data: UserDto[] }) => {
        this.studentListFromOneClass = response.data.map(student => ({
          ...student,
          createdAt: parseDate(student.createdAt),
          isPresent: false, // Default to false initially
        }));
        this.filterStudents();

        // If in edit mode, fetch students who are present
        if (this.editMode) {
          this.axiosService.request('GET', `/lessons/${lessonId}`, {}).then(
            (lessonResponse: { data: LessonDto }) => {
              console.log('Fetched lesson:', lessonResponse.data);
              this.editModeLessonDto = {
                ...lessonResponse.data,
                lessonDate: parseDate(lessonResponse.data.lessonDate),
              };

              console.log('Edit mode lesson:', this.editModeLessonDto);

              const fullDate = new Date(this.editModeLessonDto.lessonDate || '');
              this.lessonDate = fullDate.toISOString().slice(0, 10);
              this.lessonTime = fullDate.toTimeString().slice(0, 5);
              this.lessonSubject = this.editModeLessonDto.subject || '';
              this.lessonNotes = this.editModeLessonDto.content || '';

              // Fetch present students for the lesson
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
    // Combine date and time into a single Date object
    console.log(this.lessonDate, this.lessonTime);
    const [hours, minutes] = this.lessonTime.split(':').map(Number); // Extract hours and minutes
    const date = new Date(this.lessonDate); // Initialize date from lessonDate
    date.setHours(hours+1, minutes, 0, 0); // Set hours, minutes, and seconds

    const formattedDateTime = date.toISOString().slice(0, -1); // '2024-10-31T14:30:00'
    console.log('Formatted date:', formattedDateTime);


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
      } else {

      const lessonPayload = {
        lessonId: this.editModeLessonDto?.lessonId,
        createdByUser: this.editModeLessonDto?.createdByUser,
        lessonClass: this.editModeLessonDto?.lessonClass,
        lessonDate: formattedDateTime,
        subject: this.lessonSubject,
        content: this.lessonNotes
      }

      console.log(lessonPayload);

      this.axiosService.request('PUT', '/lessons/update', lessonPayload)
        .then((response: { data: string }) => {
        this.globalNotificationHandler.handleMessage(response.data);


        this.axiosService.request('PUT', `/presence/update/${this.editModeLessonDto?.lessonId}`,
          this.studentListFromOneClass
            .filter(student => student.isPresent)
            .map(student => student.id) // Send the array directly
        ).then((response: { data: string }) => {
          this.globalNotificationHandler.handleMessage(response.data);
        }).catch((error: any) => {
          console.error('Failed to update presence list');
          this.globalNotificationHandler.handleError(error);
        });






      }).catch((error: any) => {
        console.error('Failed to update lesson');
        this.globalNotificationHandler.handleError(error);
      });

    }
    this.router.navigate(['/teacher/class/' + this.classId]);
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

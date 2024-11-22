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
  studentListFromOneClass: UserDto[] = [];
  filteredStudentList: UserDto[] = [];
  classId: number | null = null;
  lessonDate: Date = new Date();
  lessonTime: string = '';
  lessonSubject: string = '';
  lessonNotes: string = '';
  searchQuery: string = '';


  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('classId'));


    this.axiosService.request('GET', `/users/class/${this.classId}/role/${2}`, {}).then(
      (response: { data: UserDto[] }) => {
        console.log('Response:', response.data);
        this.studentListFromOneClass = response.data.map(student => ({
          ...student,
          createdAt: parseDate(student.createdAt)
        }));
        console.log('Student List:', this.studentListFromOneClass);
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

    console.log('Lesson Details:', {
      subject: this.lessonSubject,
      dateTime: date,
      notes: this.lessonNotes
    });

  }

  returnToDashboard() {
    this.router.navigate(['/teacher/dashboard']);
  }


  onStudentClick(student: UserDto) {

  }
}

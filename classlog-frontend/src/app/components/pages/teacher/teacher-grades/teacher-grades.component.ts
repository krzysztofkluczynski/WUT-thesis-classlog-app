import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {ClassDto} from "../../../../model/entities/class-dto";
import {UserDto} from "../../../../model/entities/user-dto";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {parseDate} from "../../../../utils/date-utils";
import {ClassTileComponent} from "../../../shared/class-tile/class-tile.component";
import {AsyncPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {FormsModule} from '@angular/forms';
import {
  DeleteUserFromClassWindowComponent
} from "../popup-window/delete-user-from-class-window/delete-user-from-class-window.component";
import {NewGradeWindowComponent} from "../popup-window/new-grade-window/new-grade-window.component";
import {getFullName} from "../../../../utils/user-utils";
import {GradeDto} from "../../../../model/entities/grade-dto";
import {async, BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-teacher-grades',
  standalone: true,
  imports: [
    HeaderComponent,
    ClassTileComponent,
    DatePipe,
    NgForOf,
    FormsModule,
    DeleteUserFromClassWindowComponent,
    NgIf,
    NewGradeWindowComponent,
    AsyncPipe
  ],
  templateUrl: './teacher-grades.component.html',
  styleUrls: ['./teacher-grades.component.css']
})
export class TeacherGradesComponent implements OnInit {
  classList: ClassDto[] = [];
  studentListFromOneClass: UserDto[] = [];
  selectedClassId: number | null = null;
  allStudents: UserDto[] = [];
  filteredStudentList: UserDto[] = [];
  searchQuery: string = '';

  gradesFromOneClass$ = new BehaviorSubject<GradeDto[]>([]);

  showNewGradeModal: boolean = false;


  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private cdr: ChangeDetectorRef
  ) {
  }

  ngOnInit(): void {
    const storedClassId = sessionStorage.getItem('selectedClassId');
    if (storedClassId) {
      this.selectedClassId = parseInt(storedClassId, 10);
      sessionStorage.removeItem('selectedClassId'); // Clean up after restoring

      // Trigger the load for the restored class
      this.loadStudents();
    }

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
    });
  }

  loadStudents(): void {

    if (this.selectedClassId === null) {
      this.studentListFromOneClass = [];
      return;
    }

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
    });

    this.axiosService.request('GET', `/grades/class/${this.selectedClassId}`, {}).then(
      (response: { data: GradeDto[] }) => {
        const grades = response.data.map(grade => ({
          ...grade,
          createdAt: parseDate(grade.createdAt),
        }));
        this.gradesFromOneClass$.next(grades); // Update grades stream
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
    });

  }

  filterStudents(): void {
    const searchQueryLower = this.searchQuery.toLowerCase();

    this.filteredStudentList = this.allStudents.filter(student =>
      `${student.name} ${student.surname}`.toLowerCase().includes(searchQueryLower)
    );
  }

  onStudentClick(student: UserDto) {
    this.router.navigate(['/teacher/grades', student.id]);
  }

  toggleShowNewGradeModal(): void {
    this.showNewGradeModal = !this.showNewGradeModal;

    if (!this.showNewGradeModal) {
      // Save the selected class ID to sessionStorage
      if (this.selectedClassId !== null) {
        sessionStorage.setItem('selectedClassId', this.selectedClassId.toString());
      }

      // Reload the page
      window.location.reload();
    }
  }


  protected readonly getFullName = getFullName;

  addGradeToList(newGrade: GradeDto): void {
    this.gradesFromOneClass$.next([...this.gradesFromOneClass$.getValue(), newGrade]);
    console.log('Grade added to list:', this.gradesFromOneClass$);

    // Optionally fetch updated grades from the server
    if (this.selectedClassId) {
      this.axiosService.request('GET', `/grades/class/${this.selectedClassId}`, {}).then(
        (response: { data: GradeDto[] }) => {
          this.gradesFromOneClass$.next(response.data.map(grade => ({
            ...grade,
            createdAt: parseDate(grade.createdAt)
          })));
        }
      ).catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
      });
    }

    console.log('Grade added to list:', this.gradesFromOneClass$);
  }

  protected readonly async = async;
}

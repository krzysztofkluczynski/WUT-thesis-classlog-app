import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UserDto} from "../../../../../model/entities/user-dto";
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {AxiosService} from "../../../../../service/axios/axios.service";
import {AuthService} from "../../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../../service/notification/global-notification-handler.service";
import {GradeDto} from "../../../../../model/entities/grade-dto";

@Component({
  selector: 'app-new-grade-window',
  standalone: true,
  imports: [FormsModule, NgForOf, NgIf],
  templateUrl: './new-grade-window.component.html',
  styleUrls: ['./new-grade-window.component.css']
})
export class NewGradeWindowComponent implements OnInit {
  @Input() isOpen = false;
  @Input() studentListFromOneClass: UserDto[] = []; // List of students to filter
  @Input() selectedClassId: number | null = null; // Selected class ID for context
  @Output() close = new EventEmitter<void>(); // Emit event to close modal
  @Output() gradeCreated = new EventEmitter<GradeDto>(); // Emit the new grade


  studentSearchQuery: string = ''; // User input for student search
  filteredStudentList: UserDto[] = []; // Filtered list of students
  selectedStudent: UserDto | null = null; // Selected student for grading
  gradeValue: number | null = null; // Grade value input by the user
  gradeWage: number = 1; // Grade weight (1-3)
  gradeDescription: string = ''; // Description of the grade

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {
  }

  ngOnInit(): void {
    this.filteredStudentList = [...this.studentListFromOneClass];
  }

  closeWindow(): void {
    this.close.emit();
  }

  filterStudents(): void {
    const searchQueryLower = this.studentSearchQuery.trim().toLowerCase();

    this.filteredStudentList = this.studentListFromOneClass.filter(student => {
      const fullName = `${student.name} ${student.surname}`.toLowerCase();
      const reverseFullName = `${student.surname} ${student.name}`.toLowerCase();
      return fullName.includes(searchQueryLower) || reverseFullName.includes(searchQueryLower);
    });
  }

  selectStudent(student: UserDto): void {
    this.selectedStudent = student;
    this.studentSearchQuery = `${student.name} ${student.surname}`;
    this.filteredStudentList = [];
  }

  confirmSelection(): void {
    if (this.selectedStudent && this.gradeValue !== null && this.gradeValue >= 0 && this.gradeValue <= 100) {
      const gradePayload = {
        assignedClass: {
          id: this.selectedClassId
        },
        student: {
          id: this.selectedStudent.id
        },
        teacher: this.authService.getUserWithoutToken(),
        grade: this.gradeValue,
        wage: this.gradeWage,
        description: this.gradeDescription
      };

      this.axiosService.request('POST', '/grades', gradePayload)
        .then((response: { data: GradeDto }) => {
          const createdGrade = response.data;
          console.log('Grade created:', createdGrade);

          this.globalNotificationHandler.handleMessage("Grade created successfully");
          console.log('Emitting gradeCreated event:', createdGrade); // Debug log
          this.gradeCreated.emit(createdGrade);
        })
        .catch((error: any) => {
          console.error('Failed to create grade:', error);
          this.globalNotificationHandler.handleError(error);
        })
        .finally(() => {
          this.closeWindow();
        });
    } else {
      this.globalNotificationHandler.handleMessage('Please make sure all the fields are properly fullfilled.');
    }
  }
}

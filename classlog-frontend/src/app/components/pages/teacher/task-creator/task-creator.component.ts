import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {FormsModule} from "@angular/forms";
import {HeaderComponent} from "../../../shared/header/header.component";
import {NgForOf, NgIf} from "@angular/common";
import {ClassDto} from "../../../../model/entities/class-dto";
import {parseDate} from "../../../../utils/date-utils";
import {UserDto} from "../../../../model/entities/user-dto";
import {NewGradeWindowComponent} from "../popup-window/new-grade-window/new-grade-window.component";
import {AddQuestionWindowComponent} from "../popup-window/add-question-window/add-question-window.component";
import {TaskDto} from "../../../../model/entities/task-dto";

export interface ClassDtoWithSelect extends ClassDto {
  selected: boolean; // Field to track attendance
}

export interface ClosedQuestion {
  question: string;
  answer: Map<string, boolean>;
  file?: File | null;
}

export interface OpenQuestion {
  question: string;
  answer: string;
  file?: File | null;
}

@Component({
  selector: 'app-task-creator',
  standalone: true,
  imports: [
    FormsModule,
    HeaderComponent,
    NgForOf,
    NewGradeWindowComponent,
    NgIf,
    AddQuestionWindowComponent
  ],
  templateUrl: './task-creator.component.html',
  styleUrl: './task-creator.component.css'
})
export class TaskCreatorComponent implements OnInit {
  taskName: string = ''; // Task name input (string)
  taskDescription: string = ''; // Task description input (string)
  taskTime: string = ''; // Time input (string in "HH:mm" format)
  taskDate: string = ''; // Date input (string in "YYYY-MM-DD" format)
  classList: ClassDtoWithSelect[] = [];
  showAddQuestionModal: boolean = false;

  closedQuestions: ClosedQuestion[] = [];
  openQuestions: OpenQuestion[] = [];
  questionIdsFromBase: number[] = [];

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
          createdAt: parseDate(classDto.createdAt),
          selected: false
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch classes:', error);
    });
  }

  returnToDashboard() {
    this.router.navigate(['/teacher/tasks']);
  }

  createTask() {
    console.log(`${this.taskDate}T${this.taskTime}`)

    const taskData = {
      taskName: this.taskName,
      description: this.taskDescription,
      dueDate: `${this.taskDate}T${this.taskTime}`, // Combine date and time
      createdBy: this.authService.getUserWithoutToken(),
    };

    // Send task creation request to the backend
    this.axiosService.request('POST', '/tasks', taskData).then(
      (response: { data: TaskDto }) => {
        const taskId = response.data.id;
        this.globalNotificationHandler.handleMessage('Task created successfully' + response.data.taskName);

        this.axiosService
          .request('POST', `/tasks/${taskId}/assign-users`, this.classList.filter(classItem => classItem.selected))
          .then(() => {
            console.log('Task created and users assigned successfully');
            this.returnToDashboard();
          })
          .catch((error: any) => {
            this.globalNotificationHandler.handleError(error);
            console.error('Failed to assign users to the task:', error);
          });
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to create task:', error);
    });
  }


  onClassSelected(classItem: ClassDto) {
    console.log('Selected class:', classItem);
  }

  toggleAddQuestionModal() {
    this.showAddQuestionModal = !this.showAddQuestionModal;
  }

  handleQuestionSelected(question: OpenQuestion | ClosedQuestion | number): void {
    if (typeof question === 'number') {
      this.questionIdsFromBase.push(question);
      console.log('Ready Question ID:', question);
    } else if ('answer' in question && question.answer instanceof Map) {
      this.closedQuestions.push(question as ClosedQuestion);
      console.log('Closed Question:', question);
    } else {
      this.openQuestions.push(question as OpenQuestion);
      console.log('Open Question:', question);
    }
  }


}

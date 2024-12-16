import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {DatePipe, NgClass, NgForOf} from "@angular/common";
import {TaskDto} from "../../../../model/entities/task-dto";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {parseDate} from "../../../../utils/date-utils";
import {getFullName} from "../../../../utils/user-utils";

interface ExtendedTaskDto extends TaskDto {
  status: 'submitted' | 'notSubmitted';
}

@Component({
  selector: 'app-student-tasks',
  standalone: true,
  imports: [
    HeaderComponent,
    DatePipe,
    NgForOf,
    NgClass
  ],
  templateUrl: './student-tasks.component.html',
  styleUrl: './student-tasks.component.css'
})
export class StudentTasksComponent {

  waitingTasks: TaskDto[] = [];
  submittedTasks: TaskDto[] = [];
  notSubmittedTasks: TaskDto[] = [];
  overdueTasks: ExtendedTaskDto[] = []; // Combined and processed list

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {
    this.fetchWaitingTasks();
    this.fetchSubmittedTasks();
    this.fetchNotSubmittedTasks();
  }

  private fetchWaitingTasks(): void {
    this.axiosService
      .request('GET', `tasks/assignedToUser/${this.authService.getUser()?.id}/current/notSubmitted`, {})
      .then((response: { data: TaskDto[] }) => {
        this.waitingTasks = response.data.map(task => ({
          ...task,
          dueDate: parseDate(task.dueDate),
          createdAt: parseDate(task.createdAt)
        }));
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch waiting tasks:', error);
      });
  }

  private fetchSubmittedTasks(): void {
    this.axiosService
      .request('GET', `tasks/assignedToUser/${this.authService.getUser()?.id}/submitted`, {})
      .then((response: { data: TaskDto[] }) => {
        this.submittedTasks = response.data.map(task => ({
          ...task,
          dueDate: parseDate(task.dueDate),
          createdAt: parseDate(task.createdAt)
        }));
        this.combineTasks(); // Attempt to combine tasks after fetching
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch submitted tasks:', error);
      });
  }

  private fetchNotSubmittedTasks(): void {
    this.axiosService
      .request('GET', `tasks/assignedToUser/${this.authService.getUser()?.id}/overdue/notSubmitted`, {})
      .then((response: { data: TaskDto[] }) => {
        this.notSubmittedTasks = response.data.map(task => ({
          ...task,
          dueDate: parseDate(task.dueDate),
          createdAt: parseDate(task.createdAt)
        }));
        this.combineTasks(); // Attempt to combine tasks after fetching
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch not submitted tasks:', error);
      });
  }

  private combineTasks(): void {
    // Combine submitted and not submitted tasks only when both lists are fetched
    if (this.submittedTasks.length === 0 && this.notSubmittedTasks.length === 0) {
      return;
    }

    this.overdueTasks = [...this.submittedTasks, ...this.notSubmittedTasks]
      .map(task => ({
        ...task,
        status: this.submittedTasks.some(submitted => submitted.id === task.id)
          ? 'submitted'
          : 'notSubmitted'
      } as ExtendedTaskDto))
      .sort((a, b) => a.dueDate.getTime() - b.dueDate.getTime()); // Sort by due date
  }

  // Helper function to get CSS class for task status
  getTaskStatusClass(task: ExtendedTaskDto): string {
    return task.status === 'submitted' ? 'green-border' : 'red-border';
  }

  onToDoTaskClick(task: TaskDto): void {
    console.log('Task clicked:', task);
    this.router.navigate(['/student/task/solve/', task.id]);
  }

  protected readonly getFullName = getFullName;

  onSubmittedTaskClick(task: ExtendedTaskDto) {
    this.router.navigate(['/task', task.id, 'submitted', this.authService.getUser()?.id]);
  }
}

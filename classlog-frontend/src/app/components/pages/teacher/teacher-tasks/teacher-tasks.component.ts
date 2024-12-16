import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {FormsModule} from "@angular/forms";
import {DatePipe, NgClass, NgForOf} from "@angular/common";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {GradeDto} from "../../../../model/entities/grade-dto";
import {parseDate} from "../../../../utils/date-utils";
import {TaskDto} from "../../../../model/entities/task-dto";
import {UserDto} from "../../../../model/entities/user-dto";

interface UserTaskDto {
  userTaskId: number;
  user: UserDto;
  task: TaskDto;
  score: number;
}

interface UserTaskDtoExtended extends UserTaskDto {
  status: 'submitted' | 'notSubmitted';
}


@Component({
  selector: 'app-teacher-tasks',
  standalone: true,
  imports: [
    HeaderComponent,
    FormsModule,
    NgForOf,
    DatePipe,
    NgClass
  ],
  templateUrl: './teacher-tasks.component.html',
  styleUrl: './teacher-tasks.component.css'
})
export class TeacherTasksComponent {

  createdTasks: TaskDto[] = [];
  submittedTasks: UserTaskDto[] = [];
  notSubmittedTasks: UserTaskDto[] = [];
  overdueTasks: UserTaskDtoExtended[] = []; // Combined and processed list
  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  createTask() {
    this.router.navigate(['/teacher/taskCreator']);
  }

  ngOnInit(): void {
    this.fetchCreatedTasks();
    this.fetchSubmittedTasks();
    this.fetchNotSubmittedTasks();
  }

  fetchCreatedTasks(): void {
    this.axiosService.request('GET', `/tasks/createdBy/${this.authService.getUser()?.id}`, {}).then(
      (response: { data: TaskDto[] }) => {
        this.createdTasks = response.data.map(task => ({
          ...task,
          dueDate: parseDate(task.dueDate),
          createdAt: parseDate(task.createdAt)
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch tasks:', error);
    });
  }

  private fetchSubmittedTasks(): void {
    this.axiosService
      .request('GET', `tasks/createdBy/${this.authService.getUser()?.id}/submitted`, {})
      .then((response: { data: UserTaskDto[] }) => {
        this.submittedTasks = response.data.map(task => ({
          ...task,
          task: {
            ...task.task,
            dueDate: parseDate(task.task.dueDate),
            createdAt: parseDate(task.task.createdAt),
          },
          user: {
            ...task.user,
            createdAt: parseDate(task.user.createdAt),
          },
        }));
        console.log('Submitted tasks:', this.submittedTasks);
        this.combineTasks(); // Attempt to combine tasks after fetching
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch submitted tasks:', error);
      });
  }


  private fetchNotSubmittedTasks(): void {
    this.axiosService
      .request('GET', `tasks/createdBy/${this.authService.getUser()?.id}/overdue/notSubmitted`, {})
      .then((response: { data: UserTaskDto[] }) => {
        this.notSubmittedTasks = response.data.map(task => ({
          ...task,
          task: {
            ...task.task,
            dueDate: parseDate(task.task.dueDate),
            createdAt: parseDate(task.task.createdAt),
          },
          user: {
            ...task.user,
            createdAt: parseDate(task.user.createdAt),
          },
        }));
        console.log('Not submitted tasks:', this.notSubmittedTasks);
        this.combineTasks(); // Attempt to combine tasks after fetching
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch not submitted tasks:', error);
      });
  }


  onTaskClick(task: TaskDto): void {
    console.log('Task clicked:', task);
    this.router.navigate(['/teacher/taskDetails', task.id]);
  }

  deleteTask(task: TaskDto): void {
    this.axiosService.request('DELETE', `/tasks/${task.id}`, {}).then(
      (response: any) => {
        this.globalNotificationHandler.handleMessage(`Task deleted successfully`);
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to delete task:', error);
    });
  }

  private combineTasks(): void {
    // Combine submitted and notSubmitted tasks into overdueTasks
    const submittedTaskIds = new Set(this.submittedTasks.map(task => task.userTaskId)); // Track submitted task IDs

    this.overdueTasks = [...this.submittedTasks, ...this.notSubmittedTasks]
      .map(task => ({
        ...task,
        status: submittedTaskIds.has(task.userTaskId) ? 'submitted' : 'notSubmitted' // Determine status
      } as UserTaskDtoExtended)) // Explicitly assert the type as UserTaskDtoExtended
      .sort((a, b) => {
        // Ensure tasks are sorted by task's dueDate, handling missing dates
        const aDate = a.task.dueDate ? new Date(a.task.dueDate).getTime() : 0;
        const bDate = b.task.dueDate ? new Date(b.task.dueDate).getTime() : 0;
        return aDate - bDate;
      });

    console.log('Overdue tasks:', this.overdueTasks);
  }


  onSubmittedTaskClick(task: UserTaskDtoExtended) {
    this.router.navigate(['/task', task.task.id, 'submitted', task.user.id]);
  }
}

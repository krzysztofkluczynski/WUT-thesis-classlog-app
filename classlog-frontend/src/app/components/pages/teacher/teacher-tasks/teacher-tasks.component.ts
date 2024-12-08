import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {FormsModule} from "@angular/forms";
import {DatePipe, NgForOf} from "@angular/common";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {GradeDto} from "../../../../model/entities/grade-dto";
import {parseDate} from "../../../../utils/date-utils";
import {TaskDto} from "../../../../model/entities/task-dto";

@Component({
  selector: 'app-teacher-tasks',
  standalone: true,
  imports: [
    HeaderComponent,
    FormsModule,
    NgForOf,
    DatePipe
  ],
  templateUrl: './teacher-tasks.component.html',
  styleUrl: './teacher-tasks.component.css'
})
export class TeacherTasksComponent {

  createdTasks: TaskDto[] = [];
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

  onTaskClick(task: TaskDto): void {
    console.log('Task clicked:', task);
    this.router.navigate(['/teacher/taskDetails', task.id]);
  }

}

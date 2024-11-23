import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {FormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";

@Component({
  selector: 'app-teacher-tasks',
  standalone: true,
    imports: [
        HeaderComponent,
        FormsModule,
        NgForOf
    ],
  templateUrl: './teacher-tasks.component.html',
  styleUrl: './teacher-tasks.component.css'
})
export class TeacherTasksComponent {
  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  createTask() {
    this.router.navigate(['/teacher/taskCreator']);
  }
}

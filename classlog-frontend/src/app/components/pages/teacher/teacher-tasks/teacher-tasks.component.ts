import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";

@Component({
  selector: 'app-teacher-tasks',
  standalone: true,
    imports: [
        HeaderComponent
    ],
  templateUrl: './teacher-tasks.component.html',
  styleUrl: './teacher-tasks.component.css'
})
export class TeacherTasksComponent {

}

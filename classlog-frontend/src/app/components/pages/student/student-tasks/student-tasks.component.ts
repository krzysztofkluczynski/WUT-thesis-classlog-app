import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";

@Component({
  selector: 'app-student-tasks',
  standalone: true,
  imports: [
    HeaderComponent
  ],
  templateUrl: './student-tasks.component.html',
  styleUrl: './student-tasks.component.css'
})
export class StudentTasksComponent {

}

import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";

@Component({
  selector: 'app-teacher-grades',
  standalone: true,
  imports: [
    HeaderComponent
  ],
  templateUrl: './teacher-grades.component.html',
  styleUrl: './teacher-grades.component.css'
})
export class TeacherGradesComponent {

}

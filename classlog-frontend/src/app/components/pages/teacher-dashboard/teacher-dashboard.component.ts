import { Component } from '@angular/core';
import {HeaderComponent} from "../../shared/header/header.component";

@Component({
  selector: 'app-teacher-components',
  standalone: true,
  imports: [
    HeaderComponent
  ],
  templateUrl: './teacher-dashboard.component.html',
  styleUrl: './teacher-dashboard.component.css'
})
export class TeacherDashboardComponent {

}

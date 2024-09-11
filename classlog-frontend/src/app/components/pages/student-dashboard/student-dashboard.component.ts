import { Component } from '@angular/core';
import {HeaderComponent} from "../../shared/header/header.component";

@Component({
  selector: 'app-student-components',
  standalone: true,
  imports: [
    HeaderComponent
  ],
  templateUrl: './student-dashboard.component.html',
  styleUrl: './student-dashboard.component.css'
})
export class StudentDashboardComponent {

}

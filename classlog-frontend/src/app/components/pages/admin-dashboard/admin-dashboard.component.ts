import { Component } from '@angular/core';
import {HeaderComponent} from "../../shared/header/header.component";
import {LoginFormComponent} from "../starting-page/login-form/login-form.component";

@Component({
  selector: 'app-admin-components',
  standalone: true,
  imports: [
    HeaderComponent,
    LoginFormComponent
  ],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {

}

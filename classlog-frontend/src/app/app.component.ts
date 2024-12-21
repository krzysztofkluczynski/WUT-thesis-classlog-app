import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {HeaderComponent} from "./components/shared/header/header.component";
import {LoginFormComponent} from "./components/pages/login-form/login-form.component";
import {AxiosService} from "./service/axios/axios.service";
import {RegisterFormComponent} from "./components/pages/register-form/register-form.component";
import {UserProfileComponent} from "./components/shared/user-profile/user-profile.component";
import {GlobalNotificationHandler} from "./service/notification/global-notification-handler.service";
import {AdminDashboardComponent} from "./components/pages/admin/admin-dashboard/admin-dashboard.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'classlog-frontend';

}

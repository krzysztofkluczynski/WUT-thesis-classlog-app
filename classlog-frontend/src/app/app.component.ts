import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {HeaderComponent} from "./components/shared/header/header.component";
import {LoginFormComponent} from "./components/pages/login-form/login-form.component";
import {AxiosService} from "./service/axios/axios.service";
import {RegisterFormComponent} from "./components/pages/register-form/register-form.component";
import {ErrorDialogComponent} from "./components/shared/error-dialog/error-dialog.component";
import {UserProfileComponent} from "./components/pages/user-profile/user-profile.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    FormsModule,
    ReactiveFormsModule,
    HeaderComponent,
    LoginFormComponent,
    RegisterFormComponent,
    ErrorDialogComponent,
    UserProfileComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'classlog-frontend';

}

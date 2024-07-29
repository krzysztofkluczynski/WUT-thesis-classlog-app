import { Component } from '@angular/core';
import { StartingPageComponent } from "../starting-page/starting-page.component";
import { LoginFormComponent } from "../login-form/login-form.component";
import { AxiosService } from "../axios.service";
import { ButtonsComponent } from "../buttons/buttons.component";
import { AuthContentComponent } from "../auth-content/auth-content.component";
import { NgIf } from "@angular/common";

interface LoginResponse {
  data: {
    token: string;
  };
}

interface ErrorResponse {
  message: string;
}

@Component({
  selector: 'app-content',
  standalone: true,
  imports: [
    StartingPageComponent,
    LoginFormComponent,
    ButtonsComponent,
    AuthContentComponent,
    NgIf
  ],
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.css']
})
export class ContentComponent {
  componentToShow: string = "starting";

  constructor(private axiosService: AxiosService) { }

  showComponent(componentToShow: string): void {
    this.componentToShow = componentToShow;
  }

  onLogin(input: any): void {
    this.axiosService.request(
      "POST",
      "/login",
      {
        login: input.login,
        password: input.password
      }).then(
      (response: LoginResponse) => {
        this.axiosService.setAuthToken(response.data.token);
        this.componentToShow = "messages";
      }).catch(
      (error: ErrorResponse) => {
        this.axiosService.setAuthToken(null);
        this.componentToShow = "starting";
      }
    );
  }

  onRegister(input: any): void {
    this.axiosService.request(
      "POST",
      "/register",
      {
        firstName: input.firstName,
        lastName: input.lastName,
        login: input.login,
        password: input.password
      }).then(
      (response: LoginResponse) => {
        this.axiosService.setAuthToken(response.data.token);
        this.componentToShow = "messages";
      }).catch(
      (error: ErrorResponse) => {
        this.axiosService.setAuthToken(null);
        this.componentToShow = "starting";
      }
    );
  }
}

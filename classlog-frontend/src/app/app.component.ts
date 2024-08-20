import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormsModule } from "@angular/forms";

import {HeaderComponent} from "./components/shared/header/header.component";
import { AuthContentComponent } from './auth-content/auth-content.component';
import {LoginFormComponent} from "./components/pages/starting-page/login-form/login-form.component";
import {AxiosService} from "./service/axios/axios.service";
import {StartingPageComponent} from "./components/pages/starting-page/starting-page.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,
    FormsModule,
    HeaderComponent,
    AuthContentComponent,
    LoginFormComponent, StartingPageComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'classlog-frontend';


}

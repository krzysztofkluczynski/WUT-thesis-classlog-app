import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormsModule } from "@angular/forms";

import {HeaderComponent} from "./header/header.component";
import { AuthContentComponent } from './auth-content/auth-content.component';
import {ContentComponent} from "./content/content.component";
import {LoginFormComponent} from "./login-form/login-form.component";
import {StartingPageComponent} from "./starting-page/starting-page.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,
    FormsModule,
    HeaderComponent,
    AuthContentComponent,
    ContentComponent,
    LoginFormComponent,
    StartingPageComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'classlog-frontend';
}

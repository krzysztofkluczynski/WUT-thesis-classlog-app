import { Component } from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {DatePipe, NgIf} from "@angular/common";
import {ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-files',
  standalone: true,
  imports: [
    HeaderComponent,
    DatePipe,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './files.component.html',
  styleUrl: './files.component.css'
})
export class FilesComponent {

}

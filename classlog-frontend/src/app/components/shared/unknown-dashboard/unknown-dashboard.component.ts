import {Component} from '@angular/core';
import {HeaderComponent} from "../header/header.component";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'unknown-dashboard',
  standalone: true,
  imports: [
    HeaderComponent,
    NgForOf
  ],
  templateUrl: './unknown-dashboard.component.html',
  styleUrl: './unknown-dashboard.component.css'
})
export class UnknownDashboardComponent {

}

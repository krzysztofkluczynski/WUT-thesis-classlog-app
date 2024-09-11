import { Component, OnInit } from '@angular/core';
import { AxiosService } from "../service/axios/axios.service";
import { NgForOf } from "@angular/common";
//DEPRECATED
interface UserResponse {
  data: string[];
}

@Component({
  selector: 'app-auth-content',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './auth-content.component.html',
  styleUrls: ['./auth-content.component.css'] // Corrected property name
})
export class AuthContentComponent implements OnInit {
  data: string[] = [];

  constructor(private axiosService: AxiosService) { }

  ngOnInit(): void {
    this.axiosService.request('GET', '/users', {}).then(
      (response: UserResponse) => this.data = response.data
    );
  }
}

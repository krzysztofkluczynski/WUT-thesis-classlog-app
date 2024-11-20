import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgForOf} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AxiosService} from "../../../../../service/axios/axios.service";
import {AuthService} from "../../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../../service/notification/global-notification-handler.service";
import {ClassDto} from "../../../../../model/entities/class-dto";

@Component({
  selector: 'app-create-class-window',
  standalone: true,
  imports: [
    NgForOf,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './create-class-window.component.html',
  styleUrl: './create-class-window.component.css'
})
export class CreateClassWindowComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  className: string = '';
  classDescription: string = '';

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  closeWindow() {
    this.close.emit();
  }



  confirmSelection() {
    const classPayload = {
        name: this.className,
        description: this.classDescription
      };

    this.axiosService.request('POST', '/classes', classPayload)
      .then((response: { data: ClassDto }) => {
      this.globalNotificationHandler.handleMessage("Class created successfully");
    }).catch((error: any) => {
      console.error('Failed to add user to class');
      this.globalNotificationHandler.handleError(error);
    });
    this.closeWindow();
  }
}

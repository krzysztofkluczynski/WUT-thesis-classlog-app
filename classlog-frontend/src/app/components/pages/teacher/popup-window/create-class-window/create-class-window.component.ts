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
  @Output() classCreated = new EventEmitter<ClassDto>();
  className: string = '';
  classDescription: string = '';

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {
  }

  closeWindow() {
    this.close.emit();
  }


  confirmSelection() {
    // Validation for name and description lengths
    const errors: string[] = [];

    if (!this.className || this.className.trim().length === 0) {
      errors.push("Class name cannot be empty.");
    } else if (this.className.length > 250) {
      errors.push("Class name must not exceed 250 characters.");
    }

    // if (!this.classDescription || this.classDescription.trim().length === 0) {
    //   errors.push("Class description cannot be empty.");
    // } else if (this.classDescription.length > 800) {
    //   errors.push("Class description must not exceed 800 characters.");
    // }

    if (errors.length > 0) {
      this.globalNotificationHandler.handleError(errors.join(" "));
      return;
    }


    const classPayload = {
      name: this.className,
      description: this.classDescription
    };


    const requestPayload = {
      createdBy: this.authService.getUserWithoutToken(),
      classDto: classPayload
    }
    this.axiosService.request('POST', '/classes', requestPayload)
      .then((response: { data: ClassDto }) => {
        const createdClass = response.data;

        this.classCreated.emit(createdClass);

        // Notify the user of success
        this.globalNotificationHandler.handleMessage("Class created successfully");
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
      })
      .finally(() => {
        this.closeWindow();
      });
  }
}

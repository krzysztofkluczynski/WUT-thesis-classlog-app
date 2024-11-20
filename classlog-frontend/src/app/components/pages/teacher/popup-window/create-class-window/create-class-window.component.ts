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
  ) {}

  closeWindow() {
    this.close.emit();
  }



  confirmSelection() {
    // Validation for name and description lengths
    if (this.className.length > 250) {
      this.globalNotificationHandler.handleError("Class name must not exceed 250 characters.");
      return;
    }

    if (this.classDescription.length > 800) {
      this.globalNotificationHandler.handleError("Class description must not exceed 800 characters.");
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
        const createdClass = response.data; // Retrieve the created class object
        console.log('Class created:', createdClass);

        this.classCreated.emit(createdClass); // Assuming you have a classList array

        // Notify the user of success
        this.globalNotificationHandler.handleMessage("Class created successfully");
      })
      .catch((error: any) => {
        console.error('Failed to create class:', error);
        this.globalNotificationHandler.handleError(error);
      })
      .finally(() => {
        this.closeWindow(); // Close the modal regardless of success or failure
      });
  }
}

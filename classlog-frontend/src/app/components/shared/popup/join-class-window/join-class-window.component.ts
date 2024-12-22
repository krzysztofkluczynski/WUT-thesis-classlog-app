import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {AuthService} from "../../../../service/auth/auth.service";
import {AxiosService} from "../../../../service/axios/axios.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";

@Component({
  selector: 'app-join-class-window',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './join-class-window.component.html',
  styleUrl: './join-class-window.component.css'
})
export class JoinClassWindowComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  classCode: string = '';

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
  ) {}


  closeWindow() {
    this.close.emit();
  }

  confirmSelection(): void {
    this.axiosService.request('POST', '/classes/add/code', {
      classCode: this.classCode,
      user: this.authService.getUserWithoutToken()
    }).then((response: { data: string }) => {
      this.globalNotificationHandler.handleMessage(response.data);
    }).catch((error: any) => {
      console.error('Failed to add user to class');
      this.globalNotificationHandler.handleError(error);
    });
    this.closeWindow();
  }




}

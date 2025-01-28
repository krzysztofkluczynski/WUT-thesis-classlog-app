import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {AxiosService} from "../../../../../service/axios/axios.service";
import {AuthService} from "../../../../../service/auth/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../../service/notification/global-notification-handler.service";

@Component({
  selector: 'app-delete-class-window',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './delete-class-window.component.html',
  styleUrl: './delete-class-window.component.css'
})
export class DeleteClassWindowComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  classId: number | null = null;


  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('id'));
  }

  closeWindow() {
    this.close.emit();
  }


  confirmSelection() {
    if (this.classId) {
      this.axiosService.request('DELETE', `/classes/${this.classId}`, {}).then(
        (response: any) => {
          this.globalNotificationHandler.handleMessagewithType('Class deleted successfully.', 'success');
          this.close.emit();
        }
      ).catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to delete class:', error);
      }).finally(() => {
        this.router.navigate(['/teacher/dashboard']);
      });
    }
  }
}

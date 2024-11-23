import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {UserDto} from "../../../model/entities/user-dto";
import {AxiosService} from "../../../service/axios/axios.service";
import {AuthService} from "../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../service/notification/global-notification-handler.service";
import {LessonDto} from "../../../model/entities/lesson.dto";
import {parseDate} from "../../../utils/date-utils";

@Component({
  selector: 'app-lesson-info-window',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    DatePipe
  ],
  templateUrl: './lesson-info-window.component.html',
  styleUrl: './lesson-info-window.component.css'
})
export class LessonInfoWindowComponent implements OnInit {
  @Input() isOpen = false; // Controls modal visibility
  @Input() lessonID: number | null = null; // Selected class ID for context
  @Output() close = new EventEmitter<void>(); // Emit event to close modal
  @Input() classId: number | null = null;
  lessonDto: LessonDto | null = null;


  constructor(
    private axiosService: AxiosService,
    public authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {
  }

  ngOnInit(): void {
    this.axiosService.request('GET', `/lessons/${this.lessonID}`, {}).then(
      (response: { data: LessonDto }) => {
        this.lessonDto = response.data;
        this.lessonDto.lessonDate = parseDate(this.lessonDto.lessonDate);
        console.log('Fetched lesson:', this.lessonDto);
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch students:', error);
    });
  }

  editClicked() {
    this.router.navigate([`/teacher/lessonCreator/${this.classId}`], { queryParams: { editMode: true, lessonId: this.lessonID } });
  }

  deleteClicked() {
    this.axiosService.request('DELETE', `/lessons/${this.lessonID}`, {})
      .then((response: { data: string }) => {
      this.globalNotificationHandler.handleMessage(response.data);
    }).catch((error: any) => {
      console.error('Failed to delete lesson');
      this.globalNotificationHandler.handleError(error);
    });
    this.closeWindow();
  }

  closeWindow() {
    this.close.emit();
  }
}

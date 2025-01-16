import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf} from "@angular/common";
import {UserDto} from "../../../../../model/entities/user-dto";
import {AxiosService} from "../../../../../service/axios/axios.service";
import {GlobalNotificationHandler} from "../../../../../service/notification/global-notification-handler.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-show-members-window',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './show-members-window.component.html',
  styleUrl: './show-members-window.component.css'
})
export class ShowMembersWindowComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  members: UserDto[] = [];
  classId: number | null = null;

  constructor(
    private axiosService: AxiosService,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.axiosService.request('GET', `/users/class/${this.classId}`, {})
      .then((response: { data: UserDto[] }) => {
        this.members = response.data;
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
      });
  }

  closeWindow(): void {
    this.close.emit();
  }

}

import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UserDto } from '../../../../../model/entities/user-dto';
import { AxiosService } from '../../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../../service/notification/global-notification-handler.service';
import { FormsModule } from '@angular/forms';
import { NgForOf } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-delete-user-from-class-window',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './delete-user-from-class-window.component.html',
  styleUrls: ['./delete-user-from-class-window.component.css']
})
export class DeleteUserFromClassWindowComponent implements OnInit {
  @Input() isOpen = false; // To control modal visibility
  @Output() close = new EventEmitter<void>(); // Emits when modal is closed
  allUsers: UserDto[] = [];
  filteredUsers: UserDto[] = [];
  selectedUsers: UserDto[] = [];
  searchQuery: string = '';
  classId: number | null = null;

  constructor(
    private axiosService: AxiosService,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.classId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.axiosService.request('GET', `/users/class/${this.classId}`, {})
      .then((response: { data: UserDto[] }) => {
        this.allUsers = response.data.filter(user => user.role.id === 2);
        this.filteredUsers = this.allUsers;
      })
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch users:', error);
      });
  }

  filterUsers(): void {
    const query = this.searchQuery.toLowerCase();
    this.filteredUsers = this.allUsers.filter(user =>
      `${user.name} ${user.surname}`.toLowerCase().includes(query)
    );
  }

  closeWindow(): void {
    this.close.emit();
  }

  onUserClick(user: UserDto) {
    if (this.selectedUsers.includes(user)) {
      this.selectedUsers = this.selectedUsers.filter(u => u !== user);
    } else {
      this.selectedUsers.push(user);
    }
  }

  deleteUsers(): void {
    this.axiosService.request('POST', '/classes/delete/users', {
      classId: this.classId,
      users: this.selectedUsers,
    }).then((response: { data: string }) => {
      this.globalNotificationHandler.handleMessage(response.data);
    }).catch((error: any) => {
      console.error('Failed to delete users');
      this.globalNotificationHandler.handleError('Failed to delete users');
    });
    this.closeWindow();
  }

  getSelectedUsersAsString() {
    return this.selectedUsers.map(user => `${user.name} ${user.surname}`).join(', ');
  }
}

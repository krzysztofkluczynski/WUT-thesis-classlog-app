import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UserDto } from '../../../../../model/entities/user-dto';
import { AxiosService } from '../../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../../service/notification/global-notification-handler.service';
import { FormsModule } from '@angular/forms';
import { NgForOf } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-user-to-class-window',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './add-user-to-class-window.component.html',
  styleUrls: ['./add-user-to-class-window.component.css']
})
export class AddUserToClassWindowComponent implements OnInit {
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
    this.axiosService.request('GET', `/users/class/notIn/${this.classId}`, {})
      .then((response: { data: UserDto[] }) => {
        if (response.data && response.data.length > 0) {
          console.log('Users:', response.data);
          this.allUsers = response.data.filter(user => user.role && user.role.id === 2);
          this.filteredUsers = this.allUsers;
        } else {
          // Handle the empty list case
          this.allUsers = [];
          this.filteredUsers = [];
          this.globalNotificationHandler.handleMessage('No users found to display.');
        }
      })
      .catch((error: any) => {
        // Handle request errors
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

  confirmSelection(): void {
    this.axiosService.request('POST', '/classes/add/users', {
      classId: this.classId,
      users: this.selectedUsers,
    }).then((response: { data: string }) => {
      this.globalNotificationHandler.handleMessage(response.data);
      }).catch((error: any) => {
        console.error('Failed to add users to class');
        this.globalNotificationHandler.handleError('Failed to add users to class. Please try again.');
      });
    this.closeWindow();
    }

  getSelectedUsersAsString() {
    return this.selectedUsers.map(user => `${user.name} ${user.surname}`).join(', ');
  }
}

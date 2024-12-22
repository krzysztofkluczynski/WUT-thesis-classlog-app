import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AddUserToClassWindowComponent } from './add-user-to-class-window.component';
import { AxiosService } from '../../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../../service/notification/global-notification-handler.service';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MockGlobalNotificationHandler } from '../../../../../utils/tests/test-commons';
import { UserDto } from '../../../../../model/entities/user-dto';

function createMockUserDto(id: number, email: string, roleName: string): UserDto {
  return {
    id,
    name: `User${id}`,
    surname: `Surname${id}`,
    email,
    role: { id: 2, roleName },
    token: `mock-token-${id}`,
    createdAt: new Date('2023-01-01'),
  };
}

describe('AddUserToClassWindowComponent', () => {
  let component: AddUserToClassWindowComponent;
  let fixture: ComponentFixture<AddUserToClassWindowComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  const mockUsers: UserDto[] = [
    createMockUserDto(1, 'john.doe@example.com', 'Student'),
    createMockUserDto(2, 'jane.smith@example.com', 'Student'),
    createMockUserDto(3, 'alice.brown@example.com', 'Student'),
  ];

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockNotificationHandler = new MockGlobalNotificationHandler();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/users/class/notIn/1')) {
        return Promise.resolve({ data: mockUsers });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [AddUserToClassWindowComponent, FormsModule],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AddUserToClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch users not in class on initialization', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/users/class/notIn/1', {});
    expect(component.allUsers).toEqual(mockUsers);
    expect(component.filteredUsers).toEqual(mockUsers);
  });

  it('should select and deselect a user on click', () => {
    const userToSelect = mockUsers[0];

    component.onUserClick(userToSelect);
    expect(component.selectedUsers).toContain(userToSelect);

    component.onUserClick(userToSelect);
    expect(component.selectedUsers).not.toContain(userToSelect);
  });

  it('should confirm selection and make a POST request', async () => {
    component.selectedUsers = [mockUsers[0]];

    mockAxiosService.request.and.returnValue(Promise.resolve({ data: 'Users added successfully' }));

    await component.confirmSelection();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/classes/add/users', {
      classId: 1,
      users: component.selectedUsers,
    });
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Users added successfully');
  });

  it('should return selected users as a string', () => {
    component.selectedUsers = [mockUsers[0], mockUsers[1]];

    const selectedUsersString = component.getSelectedUsersAsString();

    expect(selectedUsersString).toBe('User1 Surname1, User2 Surname2');
  });

  it('should close the modal on closeWindow call', () => {
    spyOn(component.close, 'emit');

    component.closeWindow();

    expect(component.close.emit).toHaveBeenCalled();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DeleteUserFromClassWindowComponent } from './delete-user-from-class-window.component';
import { AxiosService } from '../../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../../service/notification/global-notification-handler.service';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MockGlobalNotificationHandler } from '../../../../../utils/tests/test-commons';
import { UserDto } from '../../../../../model/entities/user-dto';
import {createMockUserDto} from "../../../../../utils/create-mock-user";


describe('DeleteUserFromClassWindowComponent', () => {
  let component: DeleteUserFromClassWindowComponent;
  let fixture: ComponentFixture<DeleteUserFromClassWindowComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  const mockUsers: UserDto[] = [
    createMockUserDto(1, 'John', 'Doe'),
    createMockUserDto(2, 'Jane', 'Smith'),
    createMockUserDto(3, 'Alice', 'Johnson'),
  ];

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockNotificationHandler = new MockGlobalNotificationHandler();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/users/class/1')) {
        return Promise.resolve({ data: mockUsers });
      }
      if (url.includes('/classes/delete/users')) {
        return Promise.resolve({ data: 'Users deleted successfully' });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [DeleteUserFromClassWindowComponent, FormsModule],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DeleteUserFromClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should select and deselect users on click', () => {
    const user = mockUsers[0];

    component.onUserClick(user);
    expect(component.selectedUsers).toContain(user);

    component.onUserClick(user);
    expect(component.selectedUsers).not.toContain(user);
  });

  it('should delete selected users and close the window', async () => {
    component.selectedUsers = [mockUsers[0], mockUsers[1]];

    await component.deleteUsers();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/classes/delete/users', {
      classId: 1,
      users: component.selectedUsers,
    });
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Users deleted successfully');
    expect(component.isOpen).toBeFalse();
  });

  it('should close the window when closeWindow is called', () => {
    spyOn(component.close, 'emit');

    component.closeWindow();

    expect(component.close.emit).toHaveBeenCalled();
  });

});

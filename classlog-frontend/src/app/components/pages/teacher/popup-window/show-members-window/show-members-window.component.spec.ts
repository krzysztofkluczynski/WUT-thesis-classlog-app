import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ShowMembersWindowComponent} from './show-members-window.component';
import {AxiosService} from '../../../../../service/axios/axios.service';
import {GlobalNotificationHandler} from '../../../../../service/notification/global-notification-handler.service';
import {ActivatedRoute} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {MockGlobalNotificationHandler} from '../../../../../utils/tests/test-commons';
import {UserDto} from '../../../../../model/entities/user-dto';
import {createMockUserDto} from '../../../../../utils/create-mock-user';

describe('ShowMembersWindowComponent', () => {
  let component: ShowMembersWindowComponent;
  let fixture: ComponentFixture<ShowMembersWindowComponent>;
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
        return Promise.resolve({data: mockUsers});
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [ShowMembersWindowComponent, FormsModule],
      providers: [
        {provide: AxiosService, useValue: mockAxiosService},
        {provide: GlobalNotificationHandler, useValue: mockNotificationHandler},
        {provide: ActivatedRoute, useValue: {snapshot: {paramMap: {get: () => '1'}}}},
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowMembersWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch members for the given class ID', async () => {
    await component.fetchUsers();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/users/class/1', {});
    expect(component.members).toEqual(mockUsers);
  });

  it('should close the window when closeWindow is called', () => {
    spyOn(component.close, 'emit');

    component.closeWindow();

    expect(component.close.emit).toHaveBeenCalled();
  });
});

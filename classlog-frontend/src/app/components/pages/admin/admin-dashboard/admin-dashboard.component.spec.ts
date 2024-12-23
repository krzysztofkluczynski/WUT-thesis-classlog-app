import { TestBed, ComponentFixture } from '@angular/core/testing';
import { AdminDashboardComponent } from './admin-dashboard.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { UserDto } from "../../../../model/entities/user-dto";
import { AuthService } from "../../../../service/auth/auth.service";
import { AxiosService } from "../../../../service/axios/axios.service";
import { GlobalNotificationHandler } from "../../../../service/notification/global-notification-handler.service";
import {
  MockAuthService,
  MockAxiosService,
  MockGlobalNotificationHandler,
  MockRouter
} from "../../../../utils/tests/test-commons";

describe('AdminDashboardComponent', () => {
  let component: AdminDashboardComponent;
  let fixture: ComponentFixture<AdminDashboardComponent>;
  let mockAuthService: MockAuthService;
  let mockAxiosService: MockAxiosService;
  let mockGlobalNotificationHandler: MockGlobalNotificationHandler;
  let mockRouter: MockRouter;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminDashboardComponent],
      providers: [
        { provide: AuthService, useClass: MockAuthService },
        { provide: AxiosService, useClass: MockAxiosService },
        { provide: GlobalNotificationHandler, useClass: MockGlobalNotificationHandler },
        { provide: Router, useClass: MockRouter },
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of({ get: (key: string) => (key === 'section' ? 'teachers' : null) }),
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminDashboardComponent);
    component = fixture.componentInstance;

    // Retrieve the mock instances
    mockAuthService = TestBed.inject(AuthService) as unknown as MockAuthService;
    mockAxiosService = TestBed.inject(AxiosService) as unknown as MockAxiosService;
    mockGlobalNotificationHandler = TestBed.inject(GlobalNotificationHandler) as unknown as MockGlobalNotificationHandler;
    mockRouter = TestBed.inject(Router) as unknown as MockRouter;

    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should load users with the role "Teacher" on initialization', async () => {
    spyOn(component, 'loadUsers').and.callThrough();

    // Simulate the response from the AxiosService
    const mockUsers: UserDto[] = [
      {
        id: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com',
        role: { id: 1, roleName: 'Teacher' },
        createdAt: new Date('2024-01-01T12:00:00Z'),
        token: 'mock-token',
      },
    ];
    mockAxiosService.request.and.returnValue(Promise.resolve({ data: mockUsers }));

    await component.ngOnInit();
    expect(component.loadUsers).toHaveBeenCalledWith(1);
    expect(component.pickedRole).toEqual('Teacher');
    expect(component.usersList.length).toBeGreaterThan(0);
    expect(component.usersList[0].id).toEqual(mockUsers[0].id);
  });

  it('should navigate to user profile in edit mode', () => {
    const userId = 123;
    component.goToUserProfile(userId);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/profile', userId], { queryParams: { editMode: true } });
  });

  it('should delete a user and update the users list', async () => {
    const userId = 1;
    component.usersList = [
      {
        id: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com',
        role: { id: 1, roleName: 'Teacher' },
        createdAt: new Date('2024-01-01T12:00:00Z'),
        token: 'mock-token',
      },
    ];
    mockAxiosService.request.and.returnValue(Promise.resolve());

    await component.deleteUser(userId);

    expect(component.usersList).toEqual([]);
  });

  it('should parse createdAt dates correctly when loading users', async () => {
    const mockUsers: UserDto[] = [
      {
        id: 1,
        name: 'John',
        surname: 'Doe',
        email: 'john@example.com',
        role: { id: 1, roleName: 'Teacher' },
        createdAt: '2024-01-01T12:00:00Z' as unknown as Date,
        token: 'mock-token',
      },
    ];
    mockAxiosService.request.and.returnValue(Promise.resolve({ data: mockUsers }));

    await component.loadUsers(1);

    expect(component.usersList[0].createdAt).toEqual(new Date('2024-01-01T12:00:00Z'));
  });
});

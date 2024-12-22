import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeacherTasksComponent } from './teacher-tasks.component';
import { AxiosService } from '../../../../service/axios/axios.service';
import { AuthService } from '../../../../service/auth/auth.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { MockRouter, MockAxiosService, MockAuthService, MockGlobalNotificationHandler } from '../../../../utils/tests/test-commons';
import { TaskDto } from '../../../../model/entities/task-dto';
import { UserDto } from '../../../../model/entities/user-dto';
import {Router} from "@angular/router";

describe('TeacherTasksComponent', () => {
  let component: TeacherTasksComponent;
  let fixture: ComponentFixture<TeacherTasksComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockAuthService: MockAuthService;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockRouter: MockRouter;

  const mockTasks: TaskDto[] = [
    {
      id: 1,
      taskName: 'Task A',
      description: 'Task A Description',
      dueDate: new Date('2023-12-01'),
      createdAt: new Date('2023-11-01'),
      createdBy: {
        id: 1,
        name: 'Teacher',
        surname: 'Test',
        email: 'teacher@test.com',
        role: { id: 1, roleName: 'Teacher' },
        token: '',
        createdAt: new Date('2023-01-01'),
      },
      score: 10,
    },
    {
      id: 2,
      taskName: 'Task B',
      description: 'Task B Description',
      dueDate: new Date('2023-12-05'),
      createdAt: new Date('2023-11-02'),
      createdBy: {
        id: 1,
        name: 'Teacher',
        surname: 'Test',
        email: 'teacher@test.com',
        role: { id: 1, roleName: 'Teacher' },
        token: '',
        createdAt: new Date('2023-01-01'),
      },
      score: 20,
    },
  ];

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockAuthService = new MockAuthService();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockRouter = new MockRouter();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/tasks/createdBy') && method === 'GET') {
        return Promise.resolve({ data: mockTasks });
      }
      if (url.includes('/tasks/createdBy') && method === 'DELETE') {
        return Promise.resolve({});
      }
      if (url.includes('/tasks/createdBy') && url.includes('/submitted')) {
        return Promise.resolve({ data: [] }); // Simulated empty submitted tasks
      }
      if (url.includes('/tasks/createdBy') && url.includes('/overdue/notSubmitted')) {
        return Promise.resolve({ data: [] }); // Simulated empty not submitted tasks
      }
      return Promise.reject(new Error(`Unexpected request to ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [TeacherTasksComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TeacherTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch tasks on initialization', async () => {
    component.ngOnInit();
    await fixture.whenStable();

    expect(component.createdTasks.length).toBe(2);
    expect(component.createdTasks[0].taskName).toBe('Task A');
    expect(component.createdTasks[1].taskName).toBe('Task B');
  });

  it('should handle errors during tasks fetching', async () => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/tasks/createdBy')) {
        return Promise.reject('Error');
      }
      return Promise.resolve({});
    });

    component.fetchCreatedTasks();
    await fixture.whenStable();

    expect(mockNotificationHandler.handleError).toHaveBeenCalled();
  });
  it('should handle errors during task deletion', async () => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/tasks/createdBy') && method === 'DELETE') {
        return Promise.reject('Error');
      }
      return Promise.resolve({});
    });

    component.deleteTask(mockTasks[0]);
    await fixture.whenStable();

    expect(mockNotificationHandler.handleError).toHaveBeenCalled();
  });

  it('should navigate to task details page on task click', () => {
    component.onTaskClick(mockTasks[0]);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/teacher/taskDetails', mockTasks[0].id]);
  });

  it('should navigate to task creation page', () => {
    component.createTask();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/teacher/taskCreator']);
  });
});

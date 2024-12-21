import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StudentTasksComponent } from './student-tasks.component';
import { Router } from '@angular/router';
import { AuthService } from '../../../../service/auth/auth.service';
import { AxiosService } from '../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { MockRouter, MockAuthService, MockAxiosService, MockGlobalNotificationHandler } from '../../../../utils/tests/test-commons';
import { TaskDto } from '../../../../model/entities/task-dto';
import { createMockUserDto } from '../../../../utils/create-mock-user';

describe('StudentTasksComponent', () => {
  let component: StudentTasksComponent;
  let fixture: ComponentFixture<StudentTasksComponent>;
  let mockRouter: MockRouter;
  let mockAuthService: MockAuthService;
  let mockAxiosService: MockAxiosService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  const mockWaitingTasks: TaskDto[] = [
    {
      id: 1,
      taskName: 'Waiting Task 1',
      description: 'Description for Waiting Task 1',
      dueDate: new Date('2023-12-10T10:00:00'),
      createdAt: new Date('2023-12-01T10:00:00'),
      createdBy: createMockUserDto(1, 'Creator', 'Role'),
      score: 100,
    },
  ];

  const mockSubmittedTasks: TaskDto[] = [
    {
      id: 2,
      taskName: 'Submitted Task 1',
      description: 'Description for Submitted Task 1',
      dueDate: new Date('2023-12-05T10:00:00'),
      createdAt: new Date('2023-12-01T10:00:00'),
      createdBy: createMockUserDto(1, 'Creator', 'Role'),
      score: 80,
    },
  ];

  const mockNotSubmittedTasks: TaskDto[] = [
    {
      id: 3,
      taskName: 'Not Submitted Task 1',
      description: 'Description for Not Submitted Task 1',
      dueDate: new Date('2023-12-03T10:00:00'),
      createdAt: new Date('2023-12-01T10:00:00'),
      createdBy: createMockUserDto(1, 'Creator', 'Role'),
      score: 0,
    },
  ];

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockAuthService = new MockAuthService();
    mockAxiosService = new MockAxiosService();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    await TestBed.configureTestingModule({
      imports: [StudentTasksComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: AuthService, useValue: mockAuthService },
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(StudentTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component and fetch tasks on init', async () => {
    // Mock AxiosService responses
    mockAxiosService.request.and.callFake((method, url) => {
      if (url.includes('current/notSubmitted')) return Promise.resolve({ data: mockWaitingTasks });
      if (url.includes('submitted')) return Promise.resolve({ data: mockSubmittedTasks });
      if (url.includes('overdue/notSubmitted')) return Promise.resolve({ data: mockNotSubmittedTasks });
      return Promise.reject('Unexpected request');
    });

    // Trigger `ngOnInit` to fetch tasks
    component.ngOnInit();
    await fixture.whenStable();

    // Verify waiting tasks
    expect(component.waitingTasks.length).toBe(1);
    expect(component.waitingTasks[0].taskName).toBe('Waiting Task 1');

    // Verify submitted tasks
    expect(component.submittedTasks.length).toBe(1);
    expect(component.submittedTasks[0].taskName).toBe('Submitted Task 1');

    // Verify not submitted tasks
    expect(component.notSubmittedTasks.length).toBe(1);
    expect(component.notSubmittedTasks[0].taskName).toBe('Not Submitted Task 1');
  });

  it('should navigate to the solve task page when a task is clicked', () => {
    const task: TaskDto = {
      id: 1,
      taskName: 'Task',
      description: 'Description',
      dueDate: new Date(),
      createdAt: new Date(),
      createdBy: createMockUserDto(1, 'Creator', 'Role'),
      score: 100,
    };

    component.onToDoTaskClick(task);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/student/task/solve/', task.id]);
  });

  it('should handle errors during task fetching gracefully', async () => {
    mockAxiosService.request.and.returnValue(Promise.reject({ response: { status: 500, data: 'Error' } }));

    component.ngOnInit();
    await fixture.whenStable();

    expect(mockNotificationHandler.handleError).toHaveBeenCalled();
  });
});

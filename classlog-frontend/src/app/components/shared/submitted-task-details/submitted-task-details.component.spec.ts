import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmittedTaskDetailsComponent } from './submitted-task-details.component';
import { AxiosService } from '../../../service/axios/axios.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalNotificationHandler } from '../../../service/notification/global-notification-handler.service';
import { MockRouter, MockGlobalNotificationHandler } from '../../../utils/tests/test-commons';
import { SubmittedTaskDto } from './submitted-task-details.component';
import { UserDto } from '../../../model/entities/user-dto';
import { TaskDto } from '../../../model/entities/task-dto';
import { createMockUserDto } from '../../../utils/create-mock-user';
import {parseDate} from "../../../utils/date-utils";

describe('SubmittedTaskDetailsComponent', () => {
  let component: SubmittedTaskDetailsComponent;
  let fixture: ComponentFixture<SubmittedTaskDetailsComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockRouter: MockRouter;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  const mockUser: UserDto = createMockUserDto(1, 'john.doe@example.com', 'Student');
  const mockTask: TaskDto = {
    id: 1,
    taskName: 'Sample Task',
    description: 'This is a sample task',
    dueDate: new Date('2023-01-01T00:00:00Z'), // Match date format with `parseDate`
    createdAt: new Date('2023-01-01T00:00:00Z'),
    createdBy: mockUser,
    score: 100,
  };
  const mockSubmittedTask: SubmittedTaskDto = {
    task: mockTask,
    user: { ...mockUser, createdAt: new Date('2023-01-01T00:00:00Z')},
    questionsWithAnswers: [],
    score: 80,
  };

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request', 'requestDownload']);
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes(`/tasks/1/user/1/submitted`)) {
        return Promise.resolve({ data: mockSubmittedTask });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [SubmittedTaskDetailsComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SubmittedTaskDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch submitted task details on initialization', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', `/tasks/1/user/1/submitted`, {});
  });

  it('should handle score editing and submission', async () => {
    component.submittedTask = mockSubmittedTask;
    component.newScore = 90;

    mockAxiosService.request.and.returnValue(Promise.resolve());

    component.confirmEditScore();

    expect(mockAxiosService.request).toHaveBeenCalledWith('PUT', `/tasks/user/1/task/1/score`, {
      newScore: 90,
    });
    expect(component.submittedTask.score).toBe(80);
    expect(component.isEditingScore).toBeFalse();
  });

  it('should handle invalid score during editing', () => {
    component.submittedTask = mockSubmittedTask;
    component.newScore = 150; // Invalid score

    component.confirmEditScore();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(
      'Invalid score. Please enter a value between 0 and the maximum score.'
    );
    expect(component.isEditingScore).toBeFalse();
  });

  it('should navigate back to task list', () => {
    component.goBack();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/teacher/tasks']);
  });
});

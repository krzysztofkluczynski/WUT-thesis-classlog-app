import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskResolverComponent } from './task-resolver.component';
import { AxiosService } from '../../../../service/axios/axios.service';
import { AuthService } from '../../../../service/auth/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { MockRouter, MockGlobalNotificationHandler, MockAuthService } from '../../../../utils/tests/test-commons';
import { TaskDto } from '../../../../model/entities/task-dto';
import { QuestionWithAnswersDto } from '../../teacher/task-details/task-details.component';
import { createMockUserDto } from '../../../../utils/create-mock-user';
import { QuestionDto } from '../../../../model/entities/question-dto';
import { AnswerDto } from '../../../../model/entities/answer-dto';
import {QuestionType} from "../../../../model/entities/question-type-dto";
import {FileDto} from "../../../../model/entities/file-dto";

describe('TaskResolverComponent', () => {
  let component: TaskResolverComponent;
  let fixture: ComponentFixture<TaskResolverComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockRouter: MockRouter;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockAuthService: MockAuthService;

  const mockTask: TaskDto = {
    id: 1,
    taskName: 'Sample Task',
    description: 'This is a sample task.',
    dueDate: new Date('2024-12-31T23:59:59'),
    createdAt: new Date('2024-01-01T00:00:00'),
    createdBy: createMockUserDto(1, 'teacher@example.com', 'Teacher'),
    score: 100,
  };

  const mockQuestionsWithAnswers: QuestionWithAnswersDto[] = [
    {
      question: {
        questionId: 1,
        questionType: { questionTypeId: 1, typeName: 'Multiple Choice' },
        editedAt: new Date('2024-01-01T00:00:00'),
        points: 10,
        content: 'What is 2 + 2?',
        file: null,
      },
      answers: [
        {
          id: 1,
          question: {
            questionId: 1,
            questionType: { questionTypeId: 1, typeName: 'Multiple Choice' },
            editedAt: new Date('2024-01-01T00:00:00'),
            points: 10,
            content: 'What is 2 + 2?',
            file: null,
          },
          isCorrect: false, // Changed from `correct` to `isCorrect`
          content: '3',
        },
      ],
      file: null,
      fileUrl: null,
    },
  ];

  const setupAxiosSpies = (responses: { task?: TaskDto; questionsWithAnswers?: QuestionWithAnswersDto[]; shouldError?: boolean }) => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (responses.shouldError) {
        return Promise.reject(new Error('Request failed'));
      }
      if (url.includes('/tasks/1')) {
        return Promise.resolve({ data: responses.task || mockTask });
      }
      if (url.includes('/questions/withAnswers/1')) {
        return Promise.resolve({ data: responses.questionsWithAnswers || mockQuestionsWithAnswers });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });
  };

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request', 'requestDownload']);
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockAuthService = new MockAuthService();

    await TestBed.configureTestingModule({
      imports: [TaskResolverComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: AuthService, useValue: mockAuthService },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TaskResolverComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch task details on initialization', async () => {
    setupAxiosSpies({ task: mockTask });
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/tasks/1', {});
    expect(component.task).toEqual(mockTask);
  });

  it('should handle errors during task loading', async () => {
    setupAxiosSpies({ shouldError: true });
    await component.ngOnInit();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(jasmine.any(Error));
  });

  it('should set user answer for closed questions', () => {
    component.questionsWithAnswers = mockQuestionsWithAnswers.map(q => ({
      ...q,
      userAnswer: null,
    }));

    component.onAnswerSelected(0, '4');

    expect(component.questionsWithAnswers[0].userAnswer).toBe('4');
  });
});

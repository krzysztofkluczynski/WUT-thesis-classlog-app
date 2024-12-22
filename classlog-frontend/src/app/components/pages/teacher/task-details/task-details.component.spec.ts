import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TaskDetailsComponent } from './task-details.component';
import { ActivatedRoute, Router } from '@angular/router';
import { AxiosService } from '../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { MockRouter, MockGlobalNotificationHandler } from '../../../../utils/tests/test-commons';
import { TaskDto } from '../../../../model/entities/task-dto';
import { QuestionWithAnswersDto } from './task-details.component';
import { UserDto } from '../../../../model/entities/user-dto';
import {createMockUserDto} from "../../../../utils/create-mock-user";

describe('TaskDetailsComponent', () => {
  let component: TaskDetailsComponent;
  let fixture: ComponentFixture<TaskDetailsComponent>;
  let mockRouter: MockRouter;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  const mockTask: TaskDto = {
    id: 1,
    taskName: 'Sample Task',
    description: 'This is a sample task',
    dueDate: new Date('2023-12-31T23:59:59Z'),
    createdAt: new Date('2023-01-01T12:00:00Z'),
    createdBy: createMockUserDto(1, 'John', 'Doe'),
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
          isCorrect: true,
          content: '3',
        },
      ],
      file: null,
      fileUrl: null,
    },
  ];

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request', 'requestDownload']);
    mockNotificationHandler = new MockGlobalNotificationHandler();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/tasks/1')) {
        return Promise.resolve({ data: mockTask });
      }
      if (url.includes('/questions/withAnswers/1')) {
        return Promise.resolve({ data: mockQuestionsWithAnswers });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    mockAxiosService.requestDownload.and.callFake((url: string) => {
      if (url.includes('/files/download/1')) {
        return Promise.resolve({
          data: new Blob(['mock audio'], { type: 'audio/mpeg' }),
          headers: { 'content-type': 'audio/mpeg' },
        });
      }
      return Promise.reject(new Error(`Failed to download file: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [TaskDetailsComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TaskDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch task details on initialization', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/tasks/1', {});
    expect(component.task).toEqual(mockTask);
  });

  it('should fetch questions with answers and process them', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/questions/withAnswers/1', {});
    expect(component.questionsWithAnswers.length).toBe(1);
  });

  it('should handle errors when fetching questions with answers', async () => {
    mockAxiosService.request.and.returnValue(Promise.reject(new Error('Failed to fetch questions')));

    await component.ngOnInit();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(jasmine.any(Error));
  });


  it('should retrieve the correct answer', () => {
    const correctAnswer = component.getCorrectAnswer(mockQuestionsWithAnswers[0].answers);

    expect(correctAnswer).toBe('3');
  });

  it('should handle no correct answers gracefully', () => {
    const answers = [
      { id: 1, question: null as any, isCorrect: false, content: 'Option 1' },
      { id: 2, question: null as any, isCorrect: false, content: 'Option 2' },
    ];

    const correctAnswer = component.getCorrectAnswer(answers);

    expect(correctAnswer).toBeNull();
  });

  it('should clean up object URLs on destroy', () => {
    spyOn(URL, 'revokeObjectURL');

    component.ngOnDestroy();

    expect(URL.revokeObjectURL).toHaveBeenCalledTimes(component['objectUrls'].length);
  });

  it('should navigate back to the task list', () => {
    component.goBack();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/teacher/tasks']);
  });
});

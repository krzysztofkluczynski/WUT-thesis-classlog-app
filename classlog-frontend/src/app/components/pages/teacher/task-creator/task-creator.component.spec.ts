import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TaskCreatorComponent} from './task-creator.component';
import {Router} from '@angular/router';
import {AxiosService} from '../../../../service/axios/axios.service';
import {AuthService} from '../../../../service/auth/auth.service';
import {GlobalNotificationHandler} from '../../../../service/notification/global-notification-handler.service';
import {
  MockAuthService,
  MockAxiosService,
  MockGlobalNotificationHandler,
  MockRouter
} from '../../../../utils/tests/test-commons';
import {QuestionDto} from "../../../../model/entities/question-dto";

describe('TaskCreatorComponent', () => {
  let component: TaskCreatorComponent;
  let fixture: ComponentFixture<TaskCreatorComponent>;
  let mockRouter: MockRouter;
  let mockAxiosService: MockAxiosService;
  let mockAuthService: MockAuthService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockAxiosService = new MockAxiosService();
    mockAuthService = new MockAuthService();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.startsWith('/classes/user')) {
        return Promise.resolve({data: []});
      }
      return Promise.reject({response: {status: 400, data: {message: 'Error'}}});
    });

    await TestBed.configureTestingModule({
      imports: [TaskCreatorComponent],
      providers: [
        {provide: Router, useValue: mockRouter},
        {provide: AxiosService, useValue: mockAxiosService},
        {provide: AuthService, useValue: mockAuthService},
        {provide: GlobalNotificationHandler, useValue: mockNotificationHandler},
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TaskCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch classes on initialization', async () => {
    mockAxiosService.request.and.returnValue(Promise.resolve({data: [{id: 1, name: 'Class 1'}]}));
    await component.ngOnInit();
    expect(component.classList.length).toBe(1);
    expect(component.classList[0].name).toBe('Class 1');
  });

  it('should truncate text correctly', () => {
    expect(component.truncateText('123456')).toBe('12345...');
    expect(component.truncateText('1234')).toBe('1234');
  });

  it('should handle adding duplicate questions', () => {
    component.handleQuestionSelected(1);
    component.handleQuestionSelected(1);
    expect(component.questionIdsFromBase.length).toBe(1); // Duplicate skipped
  });

  it('should remove a ready closed question correctly', () => {
    const questionId = 1;
    component.questionIdsFromBase = [questionId];
    component.ReadyClosedQuestionsFromTheBase = [{
      questionId,
      questionType: {questionTypeId: 1, typeName: 'Closed'},
      points: 10,
      content: 'Q1'
    } as QuestionDto];
    component.removeReadyClosedQuestion(0);
    expect(component.ReadyClosedQuestionsFromTheBase.length).toBe(0);
    expect(component.questionIdsFromBase.length).toBe(0);
  });

  it('should handle duplicate question skipping', () => {
    component.handleQuestionSelected(1);
    component.handleQuestionSelected(1); // Duplicate
    expect(component.questionIdsFromBase.length).toBe(1);
  });
});

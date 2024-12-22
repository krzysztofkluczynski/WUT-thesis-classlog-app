import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';
import { AddQuestionWindowComponent } from './add-question-window.component';
import { AxiosService } from '../../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../../service/notification/global-notification-handler.service';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { QuestionDto } from '../../../../../model/entities/question-dto';
import {MockAxiosService, MockGlobalNotificationHandler} from "../../../../../utils/tests/test-commons";

describe('AddQuestionWindowComponent', () => {
  let component: AddQuestionWindowComponent;
  let fixture: ComponentFixture<AddQuestionWindowComponent>;
  let mockAxiosService: MockAxiosService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  beforeEach(async () => {
    mockAxiosService = new MockAxiosService();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    await TestBed.configureTestingModule({
      imports: [FormsModule, NgForOf, NgIf, AddQuestionWindowComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AddQuestionWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should set the active tab and fetch ready questions if none exist', () => {
    spyOn<any>(component, 'fetchReadyQuestions');
    component.readyQuestions = [];

    component.setActiveTab('ready');

    expect(component.activeTab).toBe('ready');
    expect(component['fetchReadyQuestions']).toHaveBeenCalled();
  });

  it('should set the active tab without fetching ready questions if they exist', () => {
    spyOn<any>(component, 'fetchReadyQuestions');
    component.readyQuestions = [{
      questionId: 1,
      questionType: { questionTypeId: 1, typeName: 'Multiple Choice' },
      editedAt: new Date(),
      points: 10,
      content: 'Ready Question',
      file: null
    } as QuestionDto];

    component.setActiveTab('ready');

    expect(component.activeTab).toBe('ready');
    expect(component['fetchReadyQuestions']).not.toHaveBeenCalled();
  });

  it('should emit an open question when active tab is open', () => {
    spyOn(component.questionSelected, 'emit');
    component.activeTab = 'open';
    component.openQuestionText = 'Open Question';
    component.openAnswer = 'Answer';
    component.points = 10;

    component.confirmSelection();

    expect(component.questionSelected.emit).toHaveBeenCalledWith({
      question: 'Open Question',
      answer: 'Answer',
      file: null,
      points: 10,
    });
  });

  it('should emit a close question when active tab is close', () => {
    spyOn(component.questionSelected, 'emit');
    component.activeTab = 'close';
    component.closeQuestionText = 'Close Question';
    component.closeOptions = ['Option 1', 'Option 2'];
    component.correctOption = 0;
    component.points = 5;

    component.confirmSelection();

    expect(component.questionSelected.emit).toHaveBeenCalledWith({
      question: 'Close Question',
      answer: new Map([
        ['Option 1', true],
        ['Option 2', false],
      ]),
      file: null,
      points: 5,
    });
  });

  it('should not emit anything if points are invalid', () => {
    spyOn(component.questionSelected, 'emit');
    component.points = null;

    component.confirmSelection();

    expect(component.questionSelected.emit).not.toHaveBeenCalled();
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Please enter a valid points value.');
  });

  it('should fetch ready questions and populate the list', async () => {
    const mockQuestions: QuestionDto[] = [
      {
        questionId: 1,
        questionType: { questionTypeId: 1, typeName: 'Multiple Choice' },
        editedAt: new Date(),
        points: 10,
        content: 'Ready Question 1',
        file: null
      },
      {
        questionId: 2,
        questionType: { questionTypeId: 2, typeName: 'Open Ended' },
        editedAt: new Date(),
        points: 5,
        content: 'Ready Question 2',
        file: null
      },
    ];

    mockAxiosService.request.and.returnValue(Promise.resolve({ data: mockQuestions }));

    await component['fetchReadyQuestions']();

    expect(component.readyQuestions).toEqual(mockQuestions);
  });

  it('should call closeWindow on close button click', () => {
    spyOn(component, 'closeWindow');

    const closeButton = fixture.debugElement.query(By.css('.close-button'));
    closeButton.triggerEventHandler('click', null);

    expect(component.closeWindow).toHaveBeenCalled();
  });
});

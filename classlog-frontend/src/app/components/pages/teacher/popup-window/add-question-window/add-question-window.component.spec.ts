import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { AddQuestionWindowComponent } from './add-question-window.component';
import { AxiosService } from '../../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../../service/notification/global-notification-handler.service';

describe('AddQuestionWindowComponent', () => {
  let component: AddQuestionWindowComponent;
  let fixture: ComponentFixture<AddQuestionWindowComponent>;

  const mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
  const mockGlobalNotificationHandler = jasmine.createSpyObj('GlobalNotificationHandler', ['handleError']);
  const mockActivatedRoute = jasmine.createSpyObj('ActivatedRoute', [], {});

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddQuestionWindowComponent, FormsModule, NgForOf, NgIf],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockGlobalNotificationHandler },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AddQuestionWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

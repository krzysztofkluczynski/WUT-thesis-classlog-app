import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LessonInfoWindowComponent } from './lesson-info-window.component';
import { MockRouter, MockAxiosService, MockAuthService, MockGlobalNotificationHandler } from '../../../utils/tests/test-commons';
import { Router } from '@angular/router';
import { AxiosService } from '../../../service/axios/axios.service';
import { AuthService } from '../../../service/auth/auth.service';
import { GlobalNotificationHandler } from '../../../service/notification/global-notification-handler.service';
import { LessonDto } from '../../../model/entities/lesson.dto';
import { of } from 'rxjs';

describe('LessonInfoWindowComponent', () => {
  let component: LessonInfoWindowComponent;
  let fixture: ComponentFixture<LessonInfoWindowComponent>;
  let mockRouter: MockRouter;
  let mockAxiosService: MockAxiosService;
  let mockAuthService: MockAuthService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockAxiosService = new MockAxiosService();
    mockAuthService = new MockAuthService();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    await TestBed.configureTestingModule({
      imports: [LessonInfoWindowComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LessonInfoWindowComponent);
    component = fixture.componentInstance;
  });

  it('should fetch lesson details and populate data', async () => {
    const mockLesson: LessonDto = {
      lessonId: 1,
      createdByUser: {
        id: 1,
        name: 'Teacher',
        surname: 'Test',
        email: 'teacher@test.com',
        role: { id: 1, roleName: 'Teacher' },
        token: '',
        createdAt: new Date(),
      },
      lessonClass: {
        id: 1,
        name: 'Class A',
        createdAt: new Date(),
      },
      lessonDate: new Date('2023-12-01T10:00:00'),
      subject: 'Math',
      content: 'Trigonometry basics',
    };

    // Set up mock response for AxiosService request
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url === `/lessons/1` && method === 'GET') {
        return Promise.resolve({ data: mockLesson });
      }
      return Promise.reject(new Error('Unexpected URL'));
    });

    component.lessonID = 1;
    component.ngOnInit();

    await fixture.whenStable();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', `/lessons/1`, {});
    expect(component.lessonDto).toEqual(mockLesson);
  });

  it('should handle error when fetching lesson details', async () => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url === `/lessons/1` && method === 'GET') {
        return Promise.reject(new Error('Lesson fetch failed'));
      }
      return Promise.reject(new Error('Unexpected URL'));
    });

    component.lessonID = 1;
    component.ngOnInit();

    await fixture.whenStable();

    expect(component.lessonDto).toBeNull();
  });

  it('should delete lesson and close the modal', async () => {
    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url === `/lessons/1` && method === 'DELETE') {
        return Promise.resolve({ data: 'Lesson deleted successfully' });
      }
      return Promise.reject(new Error('Unexpected URL'));
    });

    spyOn(component.close, 'emit');
    component.lessonID = 1;

    component.deleteClicked();

    await fixture.whenStable();

    expect(mockAxiosService.request).toHaveBeenCalledWith('DELETE', `/lessons/1`, {});
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Lesson deleted successfully');
    expect(component.close.emit).toHaveBeenCalled();
  });
});

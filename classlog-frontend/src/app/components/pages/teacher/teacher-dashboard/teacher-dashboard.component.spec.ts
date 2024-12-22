import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TeacherDashboardComponent } from './teacher-dashboard.component';
import { Router } from '@angular/router';
import { AxiosService } from '../../../../service/axios/axios.service';
import { AuthService } from '../../../../service/auth/auth.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { MockRouter, MockAxiosService, MockAuthService, MockGlobalNotificationHandler } from '../../../../utils/tests/test-commons';
import { ClassDto } from '../../../../model/entities/class-dto';
import { LessonDto } from '../../../../model/entities/lesson.dto';

describe('TeacherDashboardComponent', () => {
  let component: TeacherDashboardComponent;
  let fixture: ComponentFixture<TeacherDashboardComponent>;
  let mockRouter: MockRouter;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockAuthService: MockAuthService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  const mockClasses: ClassDto[] = [
    { id: 1, name: 'Class A', createdAt: new Date('2023-01-01') },
    { id: 2, name: 'Class B', createdAt: new Date('2023-02-01') },
  ];

  const mockLessons: LessonDto[] = [
    {
      lessonId: 1,
      createdByUser: {
        id: 1,
        name: 'Teacher',
        surname: 'Test',
        email: 'teacher@test.com',
        role: { id: 1, roleName: 'Teacher' },
        token: '',
        createdAt: new Date('2023-01-01'),
      },
      lessonClass: { id: 1, name: 'Class A', createdAt: new Date('2023-01-01') },
      lessonDate: new Date('2023-12-01T10:00:00'),
      subject: 'Math',
      content: 'Trigonometry basics',
    },
  ];

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockAuthService = new MockAuthService();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/classes/user')) {
        return Promise.resolve({ data: mockClasses }); // Mock classes response
      }
      if (url.includes('/users/class')) {
        return Promise.resolve({ data: [] }); // Mock teachers for each class
      }
      if (url.includes('/lessons/user')) {
        return Promise.resolve({ data: mockLessons }); // Mock lessons response
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`)); // Catch unexpected calls
    });


    await TestBed.configureTestingModule({
      imports: [TeacherDashboardComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TeacherDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch data on initialization', async () => {
    // Properly mock `request` to return promises
    mockAxiosService.request.and.callFake((method, url) => {
      if (url.includes('/classes/user')) {
        return Promise.resolve({ data: mockClasses });
      }
      if (url.includes('/lessons/user')) {
        return Promise.resolve({ data: mockLessons });
      }
      return Promise.reject('Unexpected request');
    });

    component.ngOnInit();
    await fixture.whenStable();

    expect(component.classList.length).toBe(2);
    expect(component.lessons.length).toBe(1);
    expect(component.lessons[0].subject).toBe('Math');
  });

  it('should handle errors during data fetching', async () => {
    // Mock `request` to always reject
    mockAxiosService.request.and.returnValue(Promise.reject('Error'));

    component.ngOnInit();
    await fixture.whenStable();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith('Error');
  });

  it('should toggle create class modal', () => {
    expect(component.showCreateClassModal).toBeFalse();

    component.toggleCreateClassModal();
    expect(component.showCreateClassModal).toBeTrue();

    component.toggleCreateClassModal();
    expect(component.showCreateClassModal).toBeFalse();
  });

  it('should toggle lesson modal and set selected lesson details', () => {
    component.toggleLessonWindow(mockLessons[0].lessonId, mockLessons[0].lessonClass.id);

    expect(component.selectedLessonId).toBe(mockLessons[0].lessonId);
    expect(component.classIdForSelectedLesson).toBe(mockLessons[0].lessonClass.id);
    expect(component.showLessonModal).toBeTrue();

    component.toggleLessonWindow(null, null);

    expect(component.selectedLessonId).toBeNull();
    expect(component.classIdForSelectedLesson).toBeNull();
    expect(component.showLessonModal).toBeFalse();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StudentDashboardComponent } from './student-dashboard.component';
import { Router } from '@angular/router';
import { AuthService } from '../../../../service/auth/auth.service';
import { AxiosService } from '../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import {
  MockAuthService,
  MockAxiosService,
  MockGlobalNotificationHandler,
  MockRouter,
} from '../../../../utils/tests/test-commons';

describe('StudentDashboardComponent', () => {
  let component: StudentDashboardComponent;
  let fixture: ComponentFixture<StudentDashboardComponent>;
  let mockRouter: MockRouter;
  let mockAuthService: MockAuthService;
  let mockAxiosService: MockAxiosService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  beforeEach(async () => {
    mockAuthService = new MockAuthService();
    mockAxiosService = new MockAxiosService();
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    await TestBed.configureTestingModule({
      imports: [StudentDashboardComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: AuthService, useValue: mockAuthService },
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(StudentDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch class and lesson data on init', async () => {
    const mockClasses = [
      { id: 1, name: 'Class 1', createdAt: '2023-12-01T00:00:00' },
      { id: 2, name: 'Class 2', createdAt: '2023-12-02T00:00:00' },
    ];
    const mockLessons = [
      { id: 1, lessonDate: '2023-12-01T10:00:00' },
      { id: 2, lessonDate: '2023-12-02T10:00:00' },
    ];

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
    expect(component.lessons.length).toBe(2);

    const lesson = component.lessons[0];
    if (!lesson || !(lesson.lessonDate instanceof Date)) {
      fail('lessonDate is not defined or not a Date');
    }
  });




  it('should handle errors during data fetch', async () => {
    mockAxiosService.request.and.returnValue(Promise.reject({ response: { status: 500, data: 'Error' } }));

    component.ngOnInit();
    await fixture.whenStable();

    expect(mockNotificationHandler.handleError).toHaveBeenCalled();
  });


  it('should toggle the lesson modal and update selected lesson and class IDs', () => {
    expect(component.showLessonModal).toBeFalse();
    expect(component.selectedLessonId).toBeNull();
    expect(component.classIdForSelectedLesson).toBeNull();

    component.toggleLessonWindow(1, 2);
    expect(component.showLessonModal).toBeTrue();
    expect(component.selectedLessonId).toBe(1);
    expect(component.classIdForSelectedLesson).toBe(2);

    component.toggleLessonWindow(null, null);
    expect(component.showLessonModal).toBeFalse();
    expect(component.selectedLessonId).toBeNull();
    expect(component.classIdForSelectedLesson).toBeNull();
  });

  it('should navigate to the correct class URL on class tile click', () => {
    component.onClassTileClick({ id: 1, name: 'Class 1', createdAt: new Date() });

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/student/class', 1]);
  });

  it('should return teachers for a specific class', () => {
    const mockTeachers = [
      {
        id: 1,
        name: 'Teacher 1',
        surname: 'Surname 1',
        email: 'teacher1@example.com',
        role: { id: 1, roleName: 'Teacher' },
        createdAt: new Date(),
        token: 'mock-token-1',
      },
      {
        id: 2,
        name: 'Teacher 2',
        surname: 'Surname 2',
        email: 'teacher2@example.com',
        role: { id: 1, roleName: 'Teacher' },
        createdAt: new Date(),
        token: 'mock-token-2',
      },
    ];

    const mockClass = { id: 1, name: 'Class 1', createdAt: new Date() };

    component.teachersMap.set(mockClass, mockTeachers);

    const teachers = component.getTeachersForClass(mockClass);
    expect(teachers).toEqual(mockTeachers);
  });

  it('should return an empty array if no teachers are found', () => {
    const mockClass = { id: 1, name: 'Class 1', createdAt: new Date() };

    const teachers = component.getTeachersForClass(mockClass);
    expect(teachers).toEqual([]);
  });
});

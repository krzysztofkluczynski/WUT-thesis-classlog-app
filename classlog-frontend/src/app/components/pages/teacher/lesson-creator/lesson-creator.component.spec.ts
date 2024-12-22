import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LessonCreatorComponent } from './lesson-creator.component';
import { AxiosService } from '../../../../service/axios/axios.service';
import { AuthService } from '../../../../service/auth/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { MockRouter, MockGlobalNotificationHandler, MockAuthService } from '../../../../utils/tests/test-commons';
import { UserDto } from '../../../../model/entities/user-dto';
import { LessonDto } from '../../../../model/entities/lesson.dto';
import { ClassDto } from "../../../../model/entities/class-dto";
import { createMockUserDto } from "../../../../utils/create-mock-user";

export function createMockClassDto(
  id: number,
  name: string,
  description: string = '',
  code: string = '',
  createdAt: Date = new Date()
): ClassDto {
  return {
    id,
    name,
    description,
    code,
    createdAt,
  };
}

export function createMockLessonDto(
  lessonId: number,
  createdByUser: UserDto,
  lessonClass: ClassDto,
  lessonDate: Date = new Date(),
  subject: string = '',
  content: string = ''
): LessonDto {
  return {
    lessonId,
    createdByUser,
    lessonClass,
    lessonDate,
    subject,
    content,
  };
}

describe('LessonCreatorComponent', () => {
  let component: LessonCreatorComponent;
  let fixture: ComponentFixture<LessonCreatorComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockRouter: MockRouter;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockAuthService: MockAuthService;

  const mockStudents: UserDto[] = [
    { id: 1, name: 'John', surname: 'Doe', email: 'john.doe@example.com', role: { id: 2, roleName: 'Student' }, token: '', createdAt: new Date() },
    { id: 2, name: 'Jane', surname: 'Smith', email: 'jane.smith@example.com', role: { id: 2, roleName: 'Student' }, token: '', createdAt: new Date() },
  ];

  const mockUser = createMockUserDto(1, 'teacher@example.com', 'Teacher');

  const mockClass = createMockClassDto(
    1,
    'Mathematics 101',
    'Introduction to basic mathematics',
    'MATH101',
    new Date('2024-01-01T10:00:00')
  );

  const mockLesson = createMockLessonDto(
    1,
    mockUser,
    mockClass,
    new Date('2024-01-01T10:00:00'),
    'Mathematics',
    'Lesson content about mathematics'
  );

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockAuthService = new MockAuthService();

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/users/class/1/role/2')) {
        return Promise.resolve({ data: mockStudents });
      }
      if (url.includes('/lessons/1')) {
        return Promise.resolve({ data: mockLesson });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [LessonCreatorComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' }, queryParams: { lessonId: '1', editMode: 'false' } } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LessonCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch students on initialization', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/users/class/1/role/2', {});
    expect(component.studentListFromOneClass.length).toBe(2);
    expect(component.studentListFromOneClass[0].isPresent).toBeFalse();
  });

  it('should create a new lesson', async () => {
    mockAxiosService.request.and.returnValue(Promise.resolve({ data: mockLesson }));
    component.lessonDate = '2024-01-01';
    component.lessonTime = '10:00';
    component.lessonSubject = 'Mathematics';
    component.lessonNotes = 'Lesson notes';

    await component.createLesson();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/lessons', jasmine.any(Object));
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Lesson created successfully');
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/teacher/class/1']);
  });


});

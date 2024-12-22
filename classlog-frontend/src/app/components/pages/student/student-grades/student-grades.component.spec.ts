import { ComponentFixture, TestBed } from '@angular/core/testing';
import { StudentGradesComponent } from './student-grades.component';
import { AxiosService } from '../../../../service/axios/axios.service';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { AuthService } from '../../../../service/auth/auth.service';
import {MockRouter, MockGlobalNotificationHandler, MockAuthService} from '../../../../utils/tests/test-commons';
import { UserDto } from '../../../../model/entities/user-dto';
import { GradeDto } from '../../../../model/entities/grade-dto';
import { ClassDto } from '../../../../model/entities/class-dto';
import { createMockUserDto } from '../../../../utils/create-mock-user';

describe('StudentGradesComponent', () => {
  let component: StudentGradesComponent;
  let fixture: ComponentFixture<StudentGradesComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockRouter: MockRouter;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockAuthService: MockAuthService;

  const mockUser: UserDto = createMockUserDto(1, 'john.doe@example.com', 'Student');
  const mockGrades: GradeDto[] = [
    {
    gradeId: 1,
    assignedClass: {
      id: 101,
      name: "Mathematics",
      description: "Advanced Mathematics Class",
      createdAt: new Date("2023-01-01"),
    },
    student: {
      id: 201,
      name: "John",
      surname: "Doe",
      email: "john.doe@example.com",
      role: { id: 2, roleName: "Student" },
      token: "mock-token-student",
      createdAt: new Date("2023-01-01"),
    },
    teacher: {
      id: 301,
      name: "Jane",
      surname: "Smith",
      email: "jane.smith@example.com",
      role: { id: 1, roleName: "Teacher" },
      token: "mock-token-teacher",
      createdAt: new Date("2022-12-01"),
    },
    grade: 95,
    wage: 1.5,
    description: "Final Exam Grade",
    createdAt: new Date("2023-06-15"),
  }];


  const mockTeacherClasses: ClassDto[] = [
    { id: 1, name: 'Math', description: 'Math Class', createdAt: new Date() },
  ];

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockRouter = new MockRouter();
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockAuthService = new MockAuthService();

    mockAuthService.getUser.and.returnValue(mockUser);

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes(`/users/1`)) {
        return Promise.resolve({ data: mockUser });
      }
      if (url.includes(`/grades/user/1`)) {
        return Promise.resolve({ data: mockGrades });
      }
      if (url.includes(`/classes/user/1`)) {
        return Promise.resolve({ data: mockTeacherClasses });
      }
      return Promise.reject(new Error(`Unexpected request to URL: ${url}`));
    });

    await TestBed.configureTestingModule({
      imports: [StudentGradesComponent],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: Router, useValue: mockRouter },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: AuthService, useValue: mockAuthService },
        { provide: ActivatedRoute, useValue: { params: { subscribe: (fn: (value: any) => void) => fn({ studentId: '1' }) } } },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(StudentGradesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on initialization', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/users/1', {});
    expect(component.userDto.id).toEqual(mockUser.id);
  });

  it('should fetch grades and group them by class', async () => {
    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', '/grades/user/1', {});
  });

  it('should fetch teacher classes if the user is a teacher', async () => {
    mockAuthService.getUser.and.returnValue({ ...mockUser, role: { id: 3, roleName: 'Teacher' } });

    await component.ngOnInit();

    expect(mockAxiosService.request).toHaveBeenCalledWith('GET', `/classes/user/1`, {});
    expect(component.teacherClassesIds).toEqual([1]);
  });


});

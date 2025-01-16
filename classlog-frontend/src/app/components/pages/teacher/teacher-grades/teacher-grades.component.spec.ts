import {ComponentFixture, TestBed} from '@angular/core/testing';
import {TeacherGradesComponent} from './teacher-grades.component';
import {Router} from '@angular/router';
import {AxiosService} from '../../../../service/axios/axios.service';
import {AuthService} from '../../../../service/auth/auth.service';
import {GlobalNotificationHandler} from '../../../../service/notification/global-notification-handler.service';
import {MockAuthService, MockGlobalNotificationHandler, MockRouter} from '../../../../utils/tests/test-commons';
import {ClassDto} from '../../../../model/entities/class-dto';
import {UserDto} from '../../../../model/entities/user-dto';
import {GradeDto} from '../../../../model/entities/grade-dto';

describe('TeacherGradesComponent', () => {
  let component: TeacherGradesComponent;
  let fixture: ComponentFixture<TeacherGradesComponent>;
  let mockRouter: MockRouter;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockAuthService: MockAuthService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  const mockClasses: ClassDto[] = [
    {id: 1, name: 'Class A', createdAt: new Date('2023-01-01')},
    {id: 2, name: 'Class B', createdAt: new Date('2023-02-01')},
  ];

  const mockStudents: UserDto[] = [
    {
      id: 1,
      name: 'John',
      surname: 'Doe',
      email: 'john.doe@example.com',
      role: {id: 2, roleName: 'Student'},
      token: '',
      createdAt: new Date('2023-01-01')
    },
    {
      id: 2,
      name: 'Jane',
      surname: 'Smith',
      email: 'jane.smith@example.com',
      role: {id: 2, roleName: 'Student'},
      token: '',
      createdAt: new Date('2023-02-01')
    },
  ];

  const mockGrades: GradeDto[] = [
    {
      gradeId: 1,
      assignedClass: mockClasses[0],
      student: mockStudents[0],
      teacher: {
        id: 3,
        name: 'Teacher',
        surname: 'Test',
        email: 'teacher@test.com',
        role: {id: 1, roleName: 'Teacher'},
        token: '',
        createdAt: new Date()
      },
      grade: 5,
      wage: 1.0,
      description: 'Math Test',
      createdAt: new Date('2023-12-01'),
    },
  ];

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockAuthService = new MockAuthService();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    await TestBed.configureTestingModule({
      imports: [TeacherGradesComponent],
      providers: [
        {provide: Router, useValue: mockRouter},
        {provide: AxiosService, useValue: mockAxiosService},
        {provide: AuthService, useValue: mockAuthService},
        {provide: GlobalNotificationHandler, useValue: mockNotificationHandler},
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(TeacherGradesComponent);
    component = fixture.componentInstance;

    mockAxiosService.request.and.callFake((method: string, url: string) => {
      if (url.includes('/classes/user')) {
        return Promise.resolve({data: mockClasses});
      }
      if (url.includes('/users/role/2')) {
        return Promise.resolve({data: mockStudents});
      }
      if (url.includes(`/users/class/1/role/2`)) {
        return Promise.resolve({data: mockStudents});
      }
      if (url.includes(`/grades/class/1`)) {
        return Promise.resolve({data: mockGrades});
      }
      return Promise.reject(new Error(`Unexpected request to ${url}`));
    });
  });

  afterEach(() => {
    mockAxiosService.request.calls.reset();
  });

  it('should create the component', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should fetch classes and all students on initialization', async () => {
    component.ngOnInit();
    await fixture.whenStable();

    expect(component.classList.length).toBe(2);
    expect(component.allStudents.length).toBe(2);
    expect(component.filteredStudentList.length).toBe(2);
  });

  it('should fetch students and grades for a selected class', async () => {
    component.selectedClassId = 1;
    component.loadStudents();
    await fixture.whenStable();

    // Access the value of gradesFromOneClass$ using `getValue()`
    const grades = component.gradesFromOneClass$.getValue();

    expect(component.studentListFromOneClass.length).toBe(2);
    expect(grades.length).toBe(1);
    expect(grades[0].description).toBe('Math Test');
  });


  it('should navigate to the student grades page when a student is clicked', () => {
    const student = mockStudents[0];
    component.onStudentClick(student);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/teacher/grades', student.id]);
  });
});

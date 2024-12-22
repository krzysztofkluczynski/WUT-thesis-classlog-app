import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';
import { NewGradeWindowComponent } from './new-grade-window.component';
import { AxiosService } from '../../../../../service/axios/axios.service';
import { AuthService } from '../../../../../service/auth/auth.service';
import { GlobalNotificationHandler } from '../../../../../service/notification/global-notification-handler.service';
import { Router } from '@angular/router';
import { UserDto } from '../../../../../model/entities/user-dto';
import { GradeDto } from '../../../../../model/entities/grade-dto';

describe('NewGradeWindowComponent', () => {
  let component: NewGradeWindowComponent;
  let fixture: ComponentFixture<NewGradeWindowComponent>;
  let mockAxiosService: jasmine.SpyObj<AxiosService>;
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockNotificationHandler: jasmine.SpyObj<GlobalNotificationHandler>;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    mockAxiosService = jasmine.createSpyObj('AxiosService', ['request']);
    mockAuthService = jasmine.createSpyObj('AuthService', ['getUserWithoutToken']);
    mockNotificationHandler = jasmine.createSpyObj('GlobalNotificationHandler', ['handleMessage', 'handleError']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [NewGradeWindowComponent, FormsModule, NgForOf, NgIf],
      providers: [
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(NewGradeWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize filteredStudentList with studentListFromOneClass on ngOnInit', () => {
    const mockStudents: UserDto[] = [
      { id: 1, name: 'John', surname: 'Doe', email: 'john.doe@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date(), token: 'token1' },
      { id: 2, name: 'Jane', surname: 'Smith', email: 'jane.smith@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date(), token: 'token2' },
    ];

    component.studentListFromOneClass = mockStudents;
    component.ngOnInit();

    expect(component.filteredStudentList).toEqual(mockStudents);
  });

  it('should filter students based on search query', () => {
    component.studentListFromOneClass = [
      { id: 1, name: 'John', surname: 'Doe', email: 'john.doe@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date("2024-12-22T15:00:00Z"), token: 'token1' },
      { id: 2, name: 'Jane', surname: 'Smith', email: 'jane.smith@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date("2024-12-22T15:00:00Z"), token: 'token2' },
    ];
    component.studentSearchQuery = 'Jane';

    component.filterStudents();

    expect(component.filteredStudentList).toEqual([
      { id: 2, name: 'Jane', surname: 'Smith', email: 'jane.smith@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date("2024-12-22T15:00:00Z"), token: 'token2' },
    ]);
  });

  it('should select a student and update search query', () => {
    const mockStudent: UserDto = { id: 1, name: 'John', surname: 'Doe', email: 'john.doe@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date(), token: 'token1' };

    component.selectStudent(mockStudent);

    expect(component.selectedStudent).toEqual(mockStudent);
    expect(component.studentSearchQuery).toBe('John Doe');
    expect(component.filteredStudentList).toEqual([]);
  });

  it('should emit an error message if required fields are not filled', () => {
    component.selectedStudent = null;
    component.gradeValue = null;

    component.confirmSelection();

    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Please make sure all the fields are properly fullfilled.');
  });

  it('should create a grade and emit the gradeCreated event', async () => {
    const mockGrade: GradeDto = {
      gradeId: 1,
      assignedClass: { id: 1, name: 'Class 1', description: 'Test Class' },
      student: { id: 1, name: 'John', surname: 'Doe', email: 'john.doe@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date(), token: 'token1' },
      teacher: { id: 2, name: 'Teacher', surname: 'Admin', email: 'teacher@example.com', role: { id: 1, roleName: 'Teacher' }, createdAt: new Date(), token: 'token2' },
      grade: 95,
      wage: 2,
      description: 'Excellent work',
      createdAt: new Date(),
    };

    const mockStudent: UserDto = { id: 1, name: 'John', surname: 'Doe', email: 'john.doe@example.com', role: { id: 2, roleName: 'Student' }, createdAt: new Date(), token: 'token1' };

    mockAuthService.getUserWithoutToken.and.returnValue({ id: 2, name: 'Teacher', surname: 'Admin', email: 'teacher@example.com', role: { id: 1, roleName: 'Teacher' }, createdAt: new Date() });
    mockAxiosService.request.and.returnValue(Promise.resolve({ data: mockGrade }));

    component.selectedStudent = mockStudent;
    component.selectedClassId = 1;
    component.gradeValue = 95;
    component.gradeDescription = 'Excellent work';
    component.gradeWage = 2;

    await component.confirmSelection();

    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Grade created successfully');
  });
});

import {ComponentFixture, TestBed} from '@angular/core/testing';
import {CreateClassWindowComponent} from './create-class-window.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AxiosService} from '../../../../../service/axios/axios.service';
import {AuthService} from '../../../../../service/auth/auth.service';
import {GlobalNotificationHandler} from '../../../../../service/notification/global-notification-handler.service';
import {Router} from '@angular/router';
import {ClassDto} from '../../../../../model/entities/class-dto';
import {MockAxiosService, MockGlobalNotificationHandler} from "../../../../../utils/tests/test-commons";
import {createMockUserDto} from "../../../../../utils/create-mock-user";

describe('CreateClassWindowComponent', () => {
  let component: CreateClassWindowComponent;
  let fixture: ComponentFixture<CreateClassWindowComponent>;
  let mockAxiosService: MockAxiosService;
  let mockAuthService: jasmine.SpyObj<AuthService>;
  let mockNotificationHandler: MockGlobalNotificationHandler;
  let mockRouter: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    mockAxiosService = new MockAxiosService();
    mockAuthService = jasmine.createSpyObj('AuthService', ['getUserWithoutToken']);
    mockNotificationHandler = new MockGlobalNotificationHandler();
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [CreateClassWindowComponent, FormsModule, ReactiveFormsModule],
      providers: [
        {provide: AxiosService, useValue: mockAxiosService},
        {provide: AuthService, useValue: mockAuthService},
        {provide: GlobalNotificationHandler, useValue: mockNotificationHandler},
        {provide: Router, useValue: mockRouter},
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(CreateClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should close the window when closeWindow is called', () => {
    spyOn(component.close, 'emit');

    component.closeWindow();

    expect(component.close.emit).toHaveBeenCalled();
  });

  it('should emit an error for invalid class name or description', () => {
    component.className = '';
    component.classDescription = '';

    component.confirmSelection();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(
      'Class name cannot be empty.'
    );
  });

  it('should create a class and emit the created class object', async () => {
    spyOn(component.classCreated, 'emit');
    const mockClass: ClassDto = {id: 1, name: 'Test Class', description: 'Test Description'};

    mockAuthService.getUserWithoutToken.and.returnValue(createMockUserDto(1, 'Admin', 'admin'));
    mockAxiosService.request.and.returnValue(Promise.resolve({data: mockClass}));

    component.className = 'Test Class';
    component.classDescription = 'Test Description';

    await component.confirmSelection();

    expect(component.classCreated.emit).toHaveBeenCalledWith(mockClass);
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Class created successfully');
  });

  it('should not proceed with class creation if validation fails', () => {

    component.className = '';
    component.classDescription = 'Test Description';

    component.confirmSelection();

    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith('Class name cannot be empty.');
    expect(mockAxiosService.request).not.toHaveBeenCalled();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { JoinClassWindowComponent } from './join-class-window.component';
import { MockAuthService, MockAxiosService, MockGlobalNotificationHandler } from '../../../../utils/tests/test-commons';
import { AuthService } from '../../../../service/auth/auth.service';
import { AxiosService } from '../../../../service/axios/axios.service';
import { GlobalNotificationHandler } from '../../../../service/notification/global-notification-handler.service';
import { FormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

describe('JoinClassWindowComponent', () => {
  let component: JoinClassWindowComponent;
  let fixture: ComponentFixture<JoinClassWindowComponent>;
  let mockAuthService: MockAuthService;
  let mockAxiosService: MockAxiosService;
  let mockNotificationHandler: MockGlobalNotificationHandler;

  beforeEach(async () => {
    mockAuthService = new MockAuthService();
    mockAxiosService = new MockAxiosService();
    mockNotificationHandler = new MockGlobalNotificationHandler();

    await TestBed.configureTestingModule({
      imports: [JoinClassWindowComponent, FormsModule],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: AxiosService, useValue: mockAxiosService },
        { provide: GlobalNotificationHandler, useValue: mockNotificationHandler },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(JoinClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should emit close event when closeWindow is called', () => {
    spyOn(component.close, 'emit');

    component.closeWindow();

    expect(component.close.emit).toHaveBeenCalled();
  });

  it('should call the API and handle success when confirmSelection is called', async () => {
    const mockResponse = { data: 'Class joined successfully' };
    mockAxiosService.request.and.returnValue(Promise.resolve(mockResponse));

    spyOn(component, 'closeWindow');

    component.classCode = 'TESTCODE';
    component.confirmSelection();

    await fixture.whenStable();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/classes/add/code', {
      classCode: 'TESTCODE',
      user: mockAuthService.getUserWithoutToken(),
    });
    expect(mockNotificationHandler.handleMessage).toHaveBeenCalledWith('Class joined successfully');
    expect(component.closeWindow).toHaveBeenCalled();
  });

  it('should call the API and handle error when confirmSelection is called', async () => {
    const mockError = new Error('Failed to join class');
    mockAxiosService.request.and.returnValue(Promise.reject(mockError));

    spyOn(component, 'closeWindow');

    component.classCode = 'INVALIDCODE';
    component.confirmSelection();

    await fixture.whenStable();

    expect(mockAxiosService.request).toHaveBeenCalledWith('POST', '/classes/add/code', {
      classCode: 'INVALIDCODE',
      user: mockAuthService.getUserWithoutToken(),
    });
    expect(mockNotificationHandler.handleError).toHaveBeenCalledWith(mockError);
    expect(component.closeWindow).toHaveBeenCalled();
  });

  it('should bind classCode to the input field', () => {
    const input = fixture.debugElement.query(By.css('input')).nativeElement;

    input.value = 'NEWCODE';
    input.dispatchEvent(new Event('input'));

    expect(component.classCode).toBe('NEWCODE');
  });
});

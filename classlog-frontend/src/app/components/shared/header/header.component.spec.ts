import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { MockRouter, MockAuthService, MockHeaderService } from '../../../utils/tests/test-commons';
import { HeaderOptions } from '../../../utils/header-options';
import { of, Subject } from 'rxjs';
import { Router } from '@angular/router';
import { AuthService } from '../../../service/auth/auth.service';
import { HeaderService } from '../../../service/header/header.service';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let mockRouter: MockRouter;
  let mockAuthService: MockAuthService;
  let mockHeaderService: MockHeaderService;
  let routerEventsSubject: Subject<any>;

  beforeEach(async () => {
    routerEventsSubject = new Subject(); // Dynamic events for testing
    mockRouter = new MockRouter();
    mockRouter.events = routerEventsSubject.asObservable(); // Assign dynamic events
    mockAuthService = new MockAuthService();
    mockHeaderService = new MockHeaderService();

    await TestBed.configureTestingModule({
      imports: [HeaderComponent],
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: AuthService, useValue: mockAuthService },
        { provide: HeaderService, useValue: mockHeaderService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });


  it('should call setSelectedOption and navigate based on HeaderOption', () => {
    spyOn(component, 'redirectBasedOnOption').and.callThrough();

    component.setSelectedOption(HeaderOptions.ClassesTeacher);

    expect(mockHeaderService.setSelectedOption).toHaveBeenCalledWith(HeaderOptions.ClassesTeacher);
    expect(component.redirectBasedOnOption).toHaveBeenCalledWith(HeaderOptions.ClassesTeacher);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/teacher/dashboard']);
  });

  it('should handle logout correctly', () => {
    component.logout();

    expect(mockAuthService.logout).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should navigate to the correct profile page on goToProfile', () => {
    component.goToProfile();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/profile', 1], { queryParams: { editMode: true } });
  });

  it('should subscribe to selectedOption$ and update selectedOption', () => {
    mockHeaderService.selectedOption$ = of(HeaderOptions.TasksTeacher);

    component.ngOnInit();

    expect(component.selectedOption).toBe(HeaderOptions.TasksTeacher);
  });

  it('should fallback to HeaderOptions.Students if selectedOption$ emits null', () => {
    mockHeaderService.selectedOption$ = of(null);

    component.ngOnInit();

    expect(component.selectedOption).toBe(HeaderOptions.Students);
  });

  it('should handle unknown HeaderOptions gracefully in redirectBasedOnOption', () => {
    spyOn(component, 'redirectBasedOnOption').and.callThrough();

    component.redirectBasedOnOption(HeaderOptions.Unassigned);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/admin/unassigned']);
  });

  it('should not break if router events are undefined', () => {
    // Assign `EMPTY` observable instead of `undefined` to avoid type issues
    mockRouter.events = of();

    expect(() => component.ngOnInit()).not.toThrow();
  });

  it('should not navigate if HeaderOptions is invalid in redirectBasedOnOption', () => {
    component.redirectBasedOnOption(-1 as any);

    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });

});

import { TestBed } from '@angular/core/testing';
import { GlobalNotificationHandler } from './global-notification-handler.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('GlobalNotificationHandler', () => {
  let service: GlobalNotificationHandler;
  let snackBar: MatSnackBar;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatSnackBarModule, NoopAnimationsModule], // Disable animations for testing
      providers: [GlobalNotificationHandler],
    });

    service = TestBed.inject(GlobalNotificationHandler);
    snackBar = TestBed.inject(MatSnackBar);
    spyOn(snackBar, 'open').and.callThrough(); // Spy on MatSnackBar's open method
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('handleError', () => {
    it('should display a message for backend error with details', () => {
      const backendError = {
        response: {
          status: 500,
          data: { message: 'Server error', details: 'Additional details' },
        },
        message: 'An error occurred',
      };

      service.handleError(backendError);

      expect(snackBar.open).toHaveBeenCalledWith(
        'An error occurred | Server error | Details: Additional details',
        'Close',
        jasmine.objectContaining({
          duration: 7000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['error-snackbar'],
        })
      );
    });

    it('should display a message for string error', () => {
      const errorMessage = 'A string error occurred';

      service.handleError(errorMessage);

      expect(snackBar.open).toHaveBeenCalledWith(
        'A string error occurred',
        'Close',
        jasmine.objectContaining({
          duration: 7000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['error-snackbar'],
        })
      );
    });

    it('should display a generic message for frontend error', () => {
      const frontendError = new Error('A frontend error occurred');

      service.handleError(frontendError);

      expect(snackBar.open).toHaveBeenCalledWith(
        'A frontend error occurred',
        'Close',
        jasmine.objectContaining({
          duration: 7000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['error-snackbar'],
        })
      );
    });
  });

  describe('handleMessagewithType', () => {
    it('should display a success message', () => {
      service.handleMessagewithType('Success message', 'success');

      expect(snackBar.open).toHaveBeenCalledWith(
        'Success message',
        'Close',
        jasmine.objectContaining({
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['success-snackbar'],
        })
      );
    });

    it('should display an info message', () => {
      service.handleMessagewithType('Info message', 'info');

      expect(snackBar.open).toHaveBeenCalledWith(
        'Info message',
        'Close',
        jasmine.objectContaining({
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['info-snackbar'],
        })
      );
    });

    it('should display a warning message', () => {
      service.handleMessagewithType('Warning message', 'warning');

      expect(snackBar.open).toHaveBeenCalledWith(
        'Warning message',
        'Close',
        jasmine.objectContaining({
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['warning-snackbar'],
        })
      );
    });
  });

  describe('handleMessage', () => {
    it('should display a default message', () => {
      service.handleMessage('Default message');

      expect(snackBar.open).toHaveBeenCalledWith(
        'Default message',
        'Close',
        jasmine.objectContaining({
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
        })
      );
    });
  });
});

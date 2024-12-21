import { TestBed } from '@angular/core/testing';
import { GlobalNotificationHandler } from './global-notification-handler.service';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

describe('GlobalNotificationHandler', () => {
  let service: GlobalNotificationHandler;
  let snackBar: MatSnackBar;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MatSnackBarModule],
      providers: [GlobalNotificationHandler],
    });

    service = TestBed.inject(GlobalNotificationHandler);
    snackBar = TestBed.inject(MatSnackBar);
    spyOn(snackBar, 'open').and.callThrough();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('handleError', () => {
    it('should handle an error with a response property', () => {
      const error = {
        response: {
          status: 500,
          data: {
            message: 'Internal Server Error',
            details: 'Database connection failed',
          },
        },
        message: 'An error occurred',
      };

      service.handleError(error);

      expect(snackBar.open).toHaveBeenCalledWith(
        'An error occurred | Internal Server Error | Details: Database connection failed',
        'Close',
        {
          duration: 7000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['error-snackbar'],
        }
      );
    });

    it('should handle an error without a response property', () => {
      const error = new Error('Some frontend error');

      service.handleError(error);

      expect(snackBar.open).toHaveBeenCalledWith(
        'Some frontend error',
        'Close',
        {
          duration: 7000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['error-snackbar'],
        }
      );
    });

    it('should handle an error that is a string', () => {
      const error = 'A simple error string';

      service.handleError(error);

      expect(snackBar.open).toHaveBeenCalledWith(
        'A simple error string',
        'Close',
        {
          duration: 7000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['error-snackbar'],
        }
      );
    });
  });

  describe('handleMessagewithType', () => {
    it('should handle a success message', () => {
      service.handleMessagewithType('Success message', 'success');

      expect(snackBar.open).toHaveBeenCalledWith(
        'Success message',
        'Close',
        {
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['success-snackbar'],
        }
      );
    });

    it('should handle an info message', () => {
      service.handleMessagewithType('Info message', 'info');

      expect(snackBar.open).toHaveBeenCalledWith(
        'Info message',
        'Close',
        {
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['info-snackbar'],
        }
      );
    });

    it('should handle a warning message', () => {
      service.handleMessagewithType('Warning message', 'warning');

      expect(snackBar.open).toHaveBeenCalledWith(
        'Warning message',
        'Close',
        {
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
          panelClass: ['warning-snackbar'],
        }
      );
    });
  });

  describe('handleMessage', () => {
    it('should handle a general message', () => {
      service.handleMessage('General message');

      expect(snackBar.open).toHaveBeenCalledWith(
        'General message',
        'Close',
        {
          duration: 5000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
        }
      );
    });
  });
});

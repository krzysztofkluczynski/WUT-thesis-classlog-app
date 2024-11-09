import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { ErrorResponse } from '../../model/error-response.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class GlobalNotificationHandler implements ErrorHandler {
  constructor(private snackBar: MatSnackBar) {}
  handleError(error: any): void {
    let finalMessage: string;

    if (error.response) {
      const statusCode = error.response.status;
      const message = error.message;
      const backendMessage = error.response.data?.message || 'A backend error occurred';
      const details = error.response.data?.details || null;

      finalMessage = `${message} | ${backendMessage}`;
      if (details) {
        finalMessage += ` | Details: ${details}`;
      }
    } else if (typeof error === 'string') {
      finalMessage = error;
    } else {
      finalMessage = error.message || 'A frontend error occurred';
    }

    this.snackBar.open(finalMessage, 'Close', {
      duration: 7000,
      verticalPosition: 'top',
      horizontalPosition: 'center',
      panelClass: ['error-snackbar'],
    });

    console.error('Error details:', error);
  }

  handleMessagewithType(message: string, type: 'success' | 'info' | 'warning' = 'info'): void {
    const panelClass = {
      success: 'success-snackbar',
      info: 'info-snackbar',
      warning: 'warning-snackbar',
    }[type];

    this.snackBar.open(message, 'Close', {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'center',
      panelClass: [panelClass], // Różne klasy CSS w zależności od typu wiadomości
    });
  }

  handleMessage(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      verticalPosition: 'top',
      horizontalPosition: 'center'
    });
  }

}

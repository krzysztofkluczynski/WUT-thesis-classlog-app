import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { ErrorResponse } from '../../model/error-response.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import {HttpErrorResponse} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class GlobalErrorHandler implements ErrorHandler {
  constructor(private snackBar: MatSnackBar) {}

  handleError(error: any): void {
    let finalMessage: string;

    if (error.response) {
      // Axios error structure
      const statusCode = error.response.status;
      const message = error.message;
      const backendMessage = error.response.data?.message || 'A backend error occurred';
      const details = error.response.data?.details || null;

      // Construct the full error finalMessage
      finalMessage = `${message} | ${backendMessage}`;
      if (details) {
        finalMessage += ` | Details: ${details}`;
      }
    } else if (typeof error === 'string') {
      finalMessage = error;
    } else { // Network or other errors
      finalMessage = error.message || 'A frontend error occurred';
    }

    // Display the error finalMessage in the snackbar
    this.snackBar.open(finalMessage, 'Close', {
      duration: 7000,
      verticalPosition: 'top',
      horizontalPosition: 'center',
    });
    console.error(`Error details:`, error);
  }

}

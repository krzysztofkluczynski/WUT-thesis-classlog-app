import { ErrorHandler, Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { ErrorResponse } from '../../model/error-response.model';

@Injectable({
  providedIn: 'root'
})
export class GlobalErrorHandler implements ErrorHandler {
  constructor(private router: Router, private ngZone: NgZone) {}

  handleError(error: any): void {
    if (error instanceof ErrorResponse) {
      if (error.statusCode === 403) {
        this.ngZone.run(() => this.router.navigate(['/login']));
      }
    } else {
      console.error('An unexpected error occurred:', error);
    }
  }
}

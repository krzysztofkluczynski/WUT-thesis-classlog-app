export class ErrorResponse {
  statusCode: number;
  message: string;
  details?: any;

  constructor(statusCode: number, message: string, details?: any) {
    this.statusCode = statusCode;
    this.message = message;
    this.details = details;
  }

  // Optional: Add methods to set and get properties if needed
  setStatusCode(statusCode: number): void {
    this.statusCode = statusCode;
  }

  getStatusCode(): number {
    return this.statusCode;
  }

  setMessage(message: string): void {
    this.message = message;
  }

  getMessage(): string {
    return this.message;
  }

  setDetails(details: any): void {
    this.details = details;
  }

  getDetails(): any {
    return this.details;
  }
}

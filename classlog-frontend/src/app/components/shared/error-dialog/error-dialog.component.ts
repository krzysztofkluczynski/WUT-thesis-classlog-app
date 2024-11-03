import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-error-dialog',
  standalone: true,
  templateUrl: './error-dialog.component.html',
  styleUrls: ['./error-dialog.component.css']
})
export class ErrorDialogComponent {
  @Input() errorMessage: string = '';  // Message passed from the parent
  @Output() closeDialog = new EventEmitter<void>();

  close(): void {
    this.closeDialog.emit();
  }
}

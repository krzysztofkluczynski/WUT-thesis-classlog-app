import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-new-grade-window',
  standalone: true,
  imports: [],
  templateUrl: './new-grade-window.component.html',
  styleUrl: './new-grade-window.component.css'
})
export class NewGradeWindowComponent {
  @Input() isOpen = false;
  @Output() close = new EventEmitter<void>();
  closeWindow() {
    this.close.emit();
  }

  confirmSelection() {
    this.closeWindow();
  }
}

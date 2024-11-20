import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ClassDto} from "../../../../../model/entities/class-dto";

@Component({
  selector: 'app-show-class-code-window',
  standalone: true,
  imports: [],
  templateUrl: './show-class-code-window.component.html',
  styleUrl: './show-class-code-window.component.css'
})
export class ShowClassCodeWindowComponent {
  @Input() isOpen = false;
  @Input() classDto: ClassDto | null = null;
  @Output() close = new EventEmitter<void>();

  closeWindow() {
    this.close.emit();
  }

  confirmSelection() {
    this.closeWindow();
  }
}

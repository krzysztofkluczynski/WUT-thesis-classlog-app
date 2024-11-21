import {Component, EventEmitter, Input, Output} from '@angular/core';
import {UserDto} from "../../../../../model/entities/user-dto";

@Component({
  selector: 'app-new-grade-window',
  standalone: true,
  imports: [],
  templateUrl: './new-grade-window.component.html',
  styleUrl: './new-grade-window.component.css'
})
export class NewGradeWindowComponent {
  @Input() isOpen = false;
  @Input()studentListFromOneClass: UserDto[] = [];
  @Input()selectedClassId: number | null = null;
  @Output() close = new EventEmitter<void>();

  closeWindow() {
    this.close.emit();
  }

  confirmSelection() {
    this.closeWindow();
  }
}

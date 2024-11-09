import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {HeaderOptions} from "../../utils/header-options";
@Injectable({
  providedIn: 'root'
})
export class HeaderService {
  private selectedOptionSubject = new BehaviorSubject<HeaderOptions | null>(null);
  selectedOption$ = this.selectedOptionSubject.asObservable();

  setSelectedOption(option: HeaderOptions): void {
    this.selectedOptionSubject.next(option);
  }

  getSelectedOption(): HeaderOptions | null {
    return this.selectedOptionSubject.value;
  }
}

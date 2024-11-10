import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HeaderOptions } from "../../utils/header-options";

@Injectable({
  providedIn: 'root'
})
export class HeaderService {
  private readonly storageKey = 'selectedHeaderOption';
  private selectedOptionSubject: BehaviorSubject<HeaderOptions | null>;
  selectedOption$; // Declare it here but initialize in the constructor

  constructor() {
    const savedOption = localStorage.getItem(this.storageKey) as HeaderOptions | null;
    const defaultOption = HeaderOptions.Students; // Set a sensible default
    this.selectedOptionSubject = new BehaviorSubject<HeaderOptions | null>(savedOption || defaultOption);

    // Initialize `selectedOption$` after `selectedOptionSubject`
    this.selectedOption$ = this.selectedOptionSubject.asObservable();
  }

  /**
   * Sets the selected option and saves it in localStorage.
   * @param option - The selected header option.
   */
  setSelectedOption(option: HeaderOptions): void {
    this.selectedOptionSubject.next(option);
    localStorage.setItem(this.storageKey, option);
  }

  /**
   * Gets the currently selected option.
   * @returns The selected header option or null.
   */
  getSelectedOption(): HeaderOptions | null {
    return this.selectedOptionSubject.value;
  }

  /**
   * Clears the selected option and removes it from localStorage.
   */
  clearSelectedOption(): void {
    this.selectedOptionSubject.next(null);
    localStorage.removeItem(this.storageKey);
  }
}

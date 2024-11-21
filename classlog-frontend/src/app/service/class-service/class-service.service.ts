import { Injectable } from '@angular/core';
import {ClassDto} from "../../model/entities/class-dto";

@Injectable({
  providedIn: 'root'
})
export class ClassService {
  private classDto: ClassDto | null = null;

  constructor() { }

  setClassDto(dto: ClassDto): void {
    this.classDto = dto;
  }

  getClassDto(): ClassDto | null {
    return this.classDto;
  }

  clearClassDto(): void {
    this.classDto = null;
  }
}

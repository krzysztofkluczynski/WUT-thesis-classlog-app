import {ClassDto} from "./class-dto";
import {UserDto} from "./user-dto";

export interface GradeDto {
  gradeId: number;
  assignedClass: ClassDto;
  student: UserDto;
  teacher: UserDto;
  grade: number;
  wage: number;
  description: string;
  createdAt: Date;
}

import {UserDto} from "./user-dto";
import {ClassDto} from "./class-dto";

export interface LessonDto {
  lessonId: number;
  createdBy: UserDto;
  lessonClass: ClassDto;
  lessonDate?: Date;
  subject?: string;
  content?: string;
}

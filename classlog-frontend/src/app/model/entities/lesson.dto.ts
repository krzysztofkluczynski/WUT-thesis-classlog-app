import {UserDto} from "./user-dto";
import {ClassDto} from "./class-dto";

export interface LessonDto {
  lessonId: number;
  createdByUser: UserDto;
  lessonClass: ClassDto;
  lessonDate?: Date;
  subject?: string;
  content?: string;
}

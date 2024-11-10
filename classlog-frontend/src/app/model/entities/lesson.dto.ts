import {UserDto} from "./user-dto";
import {ClassDto} from "./class-dto";

export interface LessonDto {
  id: number;
  createdBy: UserDto;
  classId: ClassDto;
  lessonDate?: Date;
  subject?: string;
  content?: string;
}

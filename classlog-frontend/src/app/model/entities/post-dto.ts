import {UserDto} from "./user-dto";
import {ClassDto} from "./class-dto";

export interface PostDto {
  id: number;
  assignedClass: ClassDto;
  user: UserDto;
  title: string;
  content: string;
  createdAt: Date;
}

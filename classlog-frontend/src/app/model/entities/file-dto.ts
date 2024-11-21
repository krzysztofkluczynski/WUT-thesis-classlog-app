import {ClassDto} from "./class-dto";
import {UserDto} from "./user-dto";

export interface FileDto {
  fileId: number;
  filePath: string;
  assignedClass: ClassDto;
  uploadedBy: UserDto;
  createdAt: Date;
}

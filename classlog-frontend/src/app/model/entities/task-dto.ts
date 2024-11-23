import {UserDto} from "./user-dto";

export interface TaskDto {
  id: number; // Task ID
  taskName: string; // Name of the task
  description: string; // Description of the task
  dueDate: string; // Due date of the task (ISO format string)
  createdAt: string; // Creation timestamp (ISO format string)
  createdBy: UserDto; // User who created the task
  score: number; // Task score
}

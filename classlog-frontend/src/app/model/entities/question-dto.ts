import {FileDto} from "./file-dto";
import {QuestionType} from "./question-type-dto";

export interface QuestionDto {
  questionId: number;
  questionType: QuestionType;
  editedAt: string; // ISO 8601 string for LocalDateTime
  points: number;
  content: string;
  fileId: FileDto | null; // Assuming fileId can be nullable
}

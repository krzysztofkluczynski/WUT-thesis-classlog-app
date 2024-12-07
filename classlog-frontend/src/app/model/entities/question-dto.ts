import {FileDto} from "./file-dto";
import {QuestionType} from "./question-type-dto";

export interface QuestionDto {
  questionId: number;
  questionType: QuestionType;
  editedAt: Date;
  points: number;
  content: string;
  fileId: FileDto | null;
}

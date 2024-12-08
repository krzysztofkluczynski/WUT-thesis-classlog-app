import {QuestionDto} from "./question-dto";

export interface AnswerDto {
  id: number,
  question: QuestionDto,
  isCorrect: boolean,
  content: string
}

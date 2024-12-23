import { Component, OnInit } from '@angular/core';
import { AxiosService } from "../../../service/axios/axios.service";
import { ActivatedRoute, Router } from "@angular/router";
import { GlobalNotificationHandler } from "../../../service/notification/global-notification-handler.service";
import { DatePipe, NgForOf, NgIf } from "@angular/common";
import { QuestionDto } from "../../../model/entities/question-dto";
import { AnswerDto } from "../../../model/entities/answer-dto";
import { TaskDto } from "../../../model/entities/task-dto";
import { UserDto } from "../../../model/entities/user-dto";
import { parseDate } from "../../../utils/date-utils";
import { FormsModule } from "@angular/forms";
import { HeaderComponent } from "../header/header.component";
import { AuthService } from "../../../service/auth/auth.service";

export interface QuestionWithAnswersAndUserAnswerDto {
  question: QuestionDto;
  answers: AnswerDto[];
  userAnswer: string | null;
  score: number;
  file: File | null;
  fileUrl: string | null;
}

export interface SubmittedTaskDto {
  task: TaskDto;
  user: UserDto;
  questionsWithAnswers: QuestionWithAnswersAndUserAnswerDto[];
  score: number;
}

@Component({
  selector: 'app-submitted-task-details',
  standalone: true,
  imports: [DatePipe, NgIf, FormsModule, HeaderComponent, NgForOf],
  templateUrl: './submitted-task-details.component.html',
  styleUrls: ['./submitted-task-details.component.css']
})
export class SubmittedTaskDetailsComponent implements OnInit {
  submittedTask!: SubmittedTaskDto;
  isEditingScore: boolean = false;
  newScore: number | null = null;
  objectUrls: string[] = [];

  constructor(
    private axiosService: AxiosService,
    public authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const taskId = this.route.snapshot.paramMap.get('taskId');
    const userId = this.route.snapshot.paramMap.get('userId');

    if (taskId && userId) {
      this.loadSubmittedTaskDetails(taskId, userId);
    } else {
      console.error('No taskId or userId provided in the route.');
    }
  }

  private loadSubmittedTaskDetails(taskId: string, userId: string): void {
    this.axiosService.request('GET', `/tasks/${taskId}/user/${userId}/submitted`, {}).then(
      (response: { data: SubmittedTaskDto }) => {
        this.submittedTask = {
          ...response.data,
          task: {
            ...response.data.task,
            createdAt: parseDate(response.data.task.createdAt),
            dueDate: parseDate(response.data.task.dueDate),
          },
          user: {
            ...response.data.user,
            createdAt: parseDate(response.data.user.createdAt),
          },
          questionsWithAnswers: response.data.questionsWithAnswers.map(question => ({
            ...question,
            question: {
              ...question.question,
              createdAt: parseDate(question.question.editedAt),
              editedAt: parseDate(question.question.editedAt),
            },
            answers: question.answers.map(answer => ({
              ...answer,
              question: {
                ...answer.question,
                createdAt: parseDate(answer.question.editedAt),
                editedAt: parseDate(answer.question.editedAt),
              },
            })),
            fileUrl: null,
          })),
        } as SubmittedTaskDto;
        this.fetchFiles();
      },
      (error: any) => {
        this.globalNotificationHandler.handleError(error);
      }
    );
  }

  private fetchFiles() {
    this.submittedTask.questionsWithAnswers.forEach((questionWithAnswers, index) => {
      const fileDto = questionWithAnswers.question.file;
      if (fileDto) {
        const fileId = fileDto.fileId;
        this.axiosService.requestDownload(`/files/download/${fileId}`, {}).then(
          (response) => {
            const contentType = response.headers["content-type"];
            if (contentType !== "audio/mpeg") {
              this.globalNotificationHandler.handleError("Some files are not in MP3 format.");
              return;
            }
            const blob = new Blob([response.data], { type: contentType });
            const url = URL.createObjectURL(blob);

            this.objectUrls.push(url);
            this.submittedTask.questionsWithAnswers[index].fileUrl = url;
            this.submittedTask.questionsWithAnswers[index].file = new File([blob], `question_${fileId}.mp3`, { type: contentType });
          },
          (error) => {
            this.globalNotificationHandler.handleError("Failed to download some files.");
          }
        );
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/teacher/tasks']);
  }

  getCorrectAnswer(answers: AnswerDto[]): string | null {
    const correctAnswer = answers.find(answer => answer.isCorrect);
    return correctAnswer ? correctAnswer.content : 'No correct answer provided';
  }

  get hasQuestions(): boolean {
    return this.submittedTask?.questionsWithAnswers?.length > 0;
  }

  editScore() {
    this.isEditingScore = true;
  }

  confirmEditScore() {
    this.isEditingScore = false;


    if (this.newScore !== null && this.newScore >= 0 && this.newScore <= this.submittedTask.task.score) {
      const taskId = this.submittedTask.task.id;
      const userId = this.submittedTask.user.id;

     const payload = {
        newScore: this.newScore
      }

      this.axiosService.request('PUT', `/tasks/user/${userId}/task/${taskId}/score`, payload)
        .then(() => {
          this.globalNotificationHandler.handleMessage("Score updated successfully.");
          if (this.newScore != null) {
            this.submittedTask.score = this.newScore;
          }
          this.newScore = null; // Reset the new score
        })
        .catch((error: any) => {
          this.globalNotificationHandler.handleError("Failed to update score. Please try again.");
          this.newScore = null; // Reset the new score on error
        });
    } else {
      this.globalNotificationHandler.handleError("Invalid score. Please enter a value between 0 and the maximum score.");
      this.newScore = null;
    }
  }


  cancelEditScore() {
    this.isEditingScore = false;
  }
}

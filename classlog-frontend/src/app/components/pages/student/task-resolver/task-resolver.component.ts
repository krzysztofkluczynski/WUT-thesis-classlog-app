import {Component, OnDestroy, OnInit} from '@angular/core';
import {TaskDto} from "../../../../model/entities/task-dto";
import {AxiosService} from "../../../../service/axios/axios.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {parseDate} from "../../../../utils/date-utils";
import {AnswerDto} from "../../../../model/entities/answer-dto";
import {QuestionWithAnswersDto} from "../../teacher/task-details/task-details.component";
import {NgForOf, NgIf} from "@angular/common";
import {HeaderComponent} from "../../../shared/header/header.component";
import {AuthService} from "../../../../service/auth/auth.service";


export interface QuestionWithUserAnswerDto extends QuestionWithAnswersDto {
  userAnswer: string | null;
}
@Component({
  selector: 'app-task-resolver',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    HeaderComponent
  ],
  templateUrl: './task-resolver.component.html',
  styleUrls: ['./task-resolver.component.css']
})
export class TaskResolverComponent implements OnInit, OnDestroy {
  task: TaskDto | null = null;
  questionsWithAnswers: QuestionWithUserAnswerDto[] = [];
  private objectUrls: string[] = [];

  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const taskId = this.route.snapshot.paramMap.get('taskId');
    if (taskId) {
      this.loadTaskDetails(taskId);
    }
  }

  ngOnDestroy(): void {
    this.cleanupObjectUrls();
  }

  private loadTaskDetails(taskId: string) {
    this.axiosService.request('GET', `/tasks/${taskId}`, {}).then(
      (response: { data: TaskDto }) => {
        this.task = response.data;
        this.loadQuestionsWithAnswers(taskId);
      },
      (error: any) => {
        this.globalNotificationHandler.handleError(error);
      }
    );
  }

  private loadQuestionsWithAnswers(taskId: string) {
    this.axiosService.request('GET', `/questions/withAnswers/${taskId}`, {}).then(
      (response: { data: QuestionWithAnswersDto[] }) => {
        this.questionsWithAnswers = response.data.map((item) => ({
          ...item,
          question: {
            ...item.question,
            editedAt: parseDate(item.question.editedAt),
          },
          fileUrl: null,
          userAnswer: null, // Initialize with null
        }));
        this.fetchFiles();
      },
      (error: any) => {
        this.globalNotificationHandler.handleError(error);
      }
    );
  }

  private fetchFiles() {
    this.questionsWithAnswers.forEach((questionWithAnswers, index) => {
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
            this.questionsWithAnswers[index].fileUrl = url;
          },
          (error) => {
            this.globalNotificationHandler.handleError("Failed to download some files.");
          }
        );
      }
    });
  }

  private cleanupObjectUrls() {
    this.objectUrls.forEach((url) => URL.revokeObjectURL(url));
  }

  goBack() {
    this.router.navigate(['/student/tasks']);
  }

  submitAnswers() {
    if (!this.task) {
      return;
    }

    const payload = {
      task: this.task,
      user: this.authService.getUserWithoutToken(), // Assuming this provides the necessary user object
      questionsWithAnswers: this.questionsWithAnswers.map((question) => ({
        question: question.question,
        answers: question.answers,
        userAnswer: question.userAnswer, // User's selected answer
        score: 0, // Include score if applicable, default to 0
      })),
    };


    this.axiosService.request('POST', '/tasks/submit', payload).then(
      (response: any) => {
        this.globalNotificationHandler.handleMessage("Answers submitted successfully.");
        this.router.navigate(['/student/tasks']);
      },
      (error: any) => {
        this.globalNotificationHandler.handleError("Failed to submit answers. Please try again.");
      }
    );
  }

  onAnswerSelected(questionIndex: number, answerContent: string) {
    this.questionsWithAnswers[questionIndex].userAnswer = answerContent;
  }

  onOpenAnswerChange(questionIndex: number, event: Event): void {
    const input = event.target as HTMLInputElement; // Cast the event target as an HTMLInputElement
    this.questionsWithAnswers[questionIndex].userAnswer = input.value; // Access the value
  }
}

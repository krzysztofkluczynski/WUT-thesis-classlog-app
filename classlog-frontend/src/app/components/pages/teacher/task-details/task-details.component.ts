import { Component, OnInit, OnDestroy } from '@angular/core';
import { HeaderComponent } from "../../../shared/header/header.component";
import { NgForOf, NgIf } from "@angular/common";
import { ActivatedRoute, Router } from "@angular/router";
import { AxiosService } from "../../../../service/axios/axios.service";
import { GlobalNotificationHandler } from "../../../../service/notification/global-notification-handler.service";
import { TaskDto } from "../../../../model/entities/task-dto";
import { QuestionDto } from "../../../../model/entities/question-dto";
import { AnswerDto } from "../../../../model/entities/answer-dto";
import { parseDate } from "../../../../utils/date-utils";

export interface QuestionWithAnswersDto {
  question: QuestionDto;
  answers: AnswerDto[];
  file: File | null;
  fileUrl: string | null; // URL for the audio playback
}

@Component({
  selector: 'app-task-details',
  standalone: true,
  imports: [HeaderComponent, NgForOf, NgIf],
  templateUrl: './task-details.component.html',
  styleUrl: './task-details.component.css',
})
export class TaskDetailsComponent implements OnInit, OnDestroy {
  task: TaskDto | null = null;
  questionsWithAnswers: QuestionWithAnswersDto[] = [];
  private objectUrls: string[] = []; // Track created object URLs for cleanup

  constructor(
    private axiosService: AxiosService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const taskId = this.route.snapshot.paramMap.get('taskId');
    if (taskId) {
      this.loadTaskDetails(taskId);
    } else {
      console.error('No taskId provided in the route.');
    }
  }

  ngOnDestroy(): void {
    this.cleanupObjectUrls();
  }

  private loadTaskDetails(taskId: string) {
    // Fetch task details
    this.axiosService.request('GET', `/tasks/${taskId}`, {}).then(
      (response: { data: TaskDto }) => {
        this.task = response.data;
        this.loadQuestionsWithAnswers(taskId);
      },
      (error: any) => {
        console.error('Failed to fetch task:', error);
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
            editedAt: parseDate(item.question.editedAt), // Parse the editedAt field
          },
          fileUrl: null, // Initialize fileUrl
        }));
        this.fetchFiles();
      },
      (error: any) => {
        console.error('Failed to fetch questions and answers data:', error);
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
              console.warn(`File ID ${fileId} is not an MP3 file.`);
              this.globalNotificationHandler.handleError("Some files are not in MP3 format.");
              return;
            }
            const blob = new Blob([response.data], { type: contentType });
            const url = URL.createObjectURL(blob);

            // Store URL for cleanup and playback
            this.objectUrls.push(url);
            this.questionsWithAnswers[index].fileUrl = url;
            this.questionsWithAnswers[index].file = new File([blob], `question_${fileId}.mp3`, { type: contentType });
          },
          (error) => {
            console.error(`Failed to download file ID ${fileId}:`, error);
            this.globalNotificationHandler.handleError("Failed to download some files.");
          }
        );
      }
    });
  }

  private cleanupObjectUrls() {
    this.objectUrls.forEach((url) => URL.revokeObjectURL(url));
    console.log('Cleaned up object URLs.');
  }

  goBack() {
    this.router.navigate(['/teacher/tasks']);
  }
}

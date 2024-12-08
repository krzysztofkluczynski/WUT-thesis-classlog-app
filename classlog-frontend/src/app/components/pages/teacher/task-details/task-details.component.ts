import {Component, OnInit} from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {NgForOf, NgIf} from "@angular/common";
import {PostDto} from "../../../../model/entities/post-dto";
import {CommentDto} from "../../../../model/entities/comment-dto";
import {LessonDto} from "../../../../model/entities/lesson.dto";
import {ClassDto} from "../../../../model/entities/class-dto";
import {AuthService} from "../../../../service/auth/auth.service";
import {AxiosService} from "../../../../service/axios/axios.service";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {ClassService} from "../../../../service/class-service/class-service.service";
import {TaskDto} from "../../../../model/entities/task-dto";
import {QuestionDto} from "../../../../model/entities/question-dto";
import {AnswerDto} from "../../../../model/entities/answer-dto";
import {parseDate} from "../../../../utils/date-utils";

export interface QuestionWithAnswersDto {
  question: QuestionDto,
  answers: AnswerDto[],
  file: File | null
}

@Component({
  selector: 'app-task-details',
  standalone: true,
  imports: [
    HeaderComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './task-details.component.html',
  styleUrl: './task-details.component.css'
})

export class TaskDetailsComponent implements OnInit {
  task: TaskDto | null = null;
  questionsWithAnswers: QuestionWithAnswersDto[] = [];

  constructor(
    private authService: AuthService,
    private axiosService: AxiosService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute,
    private classService: ClassService
  ) {}

  ngOnInit(): void {
    const taskId = this.route.snapshot.paramMap.get('taskId');
    console.log('Task ID:', taskId);

    if (taskId) {
      // Fetch task details
      this.axiosService.request('GET', `/tasks/${taskId}`, {}).then(
        (response: { data: TaskDto }) => {
          this.task = response.data;

          // Fetch questions with answers after task is loaded
          this.axiosService.request('GET', `/questions/withAnswers/${taskId}`, {}).then(
            (response: { data: QuestionWithAnswersDto[] }) => {
              this.questionsWithAnswers = response.data.map((item: QuestionWithAnswersDto) => ({
                ...item,
                question: {
                  ...item.question,
                  editedAt: parseDate(item.question.editedAt), // Parse the editedAt field
                }
              }));
              this.fetchFiles();
            }
          ).catch((error: any) => {
            this.globalNotificationHandler.handleError(error);
            console.error('Failed to fetch questions and answers data:', error);
          });
        }
      ).catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to fetch task:', error);
      });
    } else {
      console.error('No taskId provided in the route.');
    }

  }

  private fetchFiles() {
    this.questionsWithAnswers.forEach((questionWithAnswers, index) => {
      console.log('Fetching file:', questionWithAnswers.file)
      // Check if the question has a fileDto and is not null
      if (questionWithAnswers.question.file) {
        const fileId = questionWithAnswers.question.file.fileId;

        // Make the request to download the file
        this.axiosService
          .requestDownload(`/files/download/${fileId}`, {})
          .then((response) => {
            // Ensure the file is in MP3 format
            const contentType = response.headers["content-type"];
            if (contentType !== "audio/mpeg") {
              console.error("The file is not an MP3 format.");
              this.globalNotificationHandler.handleError("Some file is not an MP3 format.");
              return;
            }

            // Create a Blob from the response data
            const blob = new Blob([response.data], { type: contentType });

            // Set the file in the corresponding QuestionWithAnswersDto
            this.questionsWithAnswers[index].file = new File([blob], `question_${fileId}.mp3`, { type: contentType });
            console.log(`MP3 file for question ID ${fileId} downloaded and set.`);
          })
          .catch((error) => {
            console.error("Failed to download file:", error);
            this.globalNotificationHandler.handleError("Failed to download file.");
          });
      }
    });
    console.log('Fetched files for questions:', this.questionsWithAnswers);
  }



  goBack() {
    this.router.navigate(['/teacher/tasks']);
  }
}

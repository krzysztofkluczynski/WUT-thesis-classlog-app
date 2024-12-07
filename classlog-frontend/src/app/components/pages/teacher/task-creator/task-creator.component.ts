import {Component, OnInit} from '@angular/core';
import {AxiosService} from "../../../../service/axios/axios.service";
import {AuthService} from "../../../../service/auth/auth.service";
import {Router} from "@angular/router";
import {GlobalNotificationHandler} from "../../../../service/notification/global-notification-handler.service";
import {FormsModule} from "@angular/forms";
import {HeaderComponent} from "../../../shared/header/header.component";
import {NgForOf, NgIf} from "@angular/common";
import {ClassDto} from "../../../../model/entities/class-dto";
import {parseDate} from "../../../../utils/date-utils";
import {UserDto} from "../../../../model/entities/user-dto";
import {NewGradeWindowComponent} from "../popup-window/new-grade-window/new-grade-window.component";
import {AddQuestionWindowComponent} from "../popup-window/add-question-window/add-question-window.component";
import {TaskDto} from "../../../../model/entities/task-dto";
import {FileDto} from "../../../../model/entities/file-dto";
import {QuestionDto} from "../../../../model/entities/question-dto";

export interface ClassDtoWithSelect extends ClassDto {
  selected: boolean; // Field to track attendance
}

export interface ClosedQuestion {
  question: string;
  answer: Map<string, boolean>;
  file?: File | null;
  points: number;
}

export interface OpenQuestion {
  question: string;
  answer: string;
  file?: File | null;
  points: number;
}

@Component({
  selector: 'app-task-creator',
  standalone: true,
  imports: [
    FormsModule,
    HeaderComponent,
    NgForOf,
    NewGradeWindowComponent,
    NgIf,
    AddQuestionWindowComponent
  ],
  templateUrl: './task-creator.component.html',
  styleUrl: './task-creator.component.css'
})
export class TaskCreatorComponent implements OnInit {
  taskName: string = ''; // Task name input (string)
  taskDescription: string = ''; // Task description input (string)
  taskTime: string = ''; // Time input (string in "HH:mm" format)
  taskDate: string = ''; // Date input (string in "YYYY-MM-DD" format)
  classList: ClassDtoWithSelect[] = [];
  showAddQuestionModal: boolean = false;

  closedQuestions: ClosedQuestion[] = [];
  openQuestions: OpenQuestion[] = [];
  questionIdsFromBase: number[] = [];
  ReadyOpenQuestionsFromTheBase: QuestionDto[] = [];
  ReadyClosedQuestionsFromTheBase: QuestionDto[] = [];

  createdTask: TaskDto | null = null;
  constructor(
    private axiosService: AxiosService,
    private authService: AuthService,
    private router: Router,
    private globalNotificationHandler: GlobalNotificationHandler
  ) {}

  ngOnInit(): void {
    // Fetch classes for the current teacher
    this.axiosService.request('GET', `/classes/user/${this.authService.getUser()?.id}`, {}).then(
      (response: { data: ClassDto[] }) => {
        this.classList = response.data.map(classDto => ({
          ...classDto,
          createdAt: parseDate(classDto.createdAt),
          selected: false
        }));
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch classes:', error);
    });
  }

  returnToDashboard() {
    this.router.navigate(['/teacher/tasks']);
  }

  createTask() {
    console.log(`${this.taskDate}T${this.taskTime}`);

    let score: number = this.calculateScore();

    const taskData = {
      taskName: this.taskName,
      description: this.taskDescription,
      dueDate: `${this.taskDate}T${this.taskTime}`, // Combine date and time
      createdBy: this.authService.getUserWithoutToken(),
      score: score
    };

    // Send task creation request to the backend
    this.axiosService.request('POST', '/tasks', taskData).then(
      (response: { data: TaskDto }) => {
        this.createdTask = response.data;
        this.globalNotificationHandler.handleMessage(
          'Task created successfully: ' + response.data.taskName
        );

        // Assign users to the task
        this.axiosService
          .request(
            'POST',
            `/tasks/${this.createdTask.id}/assign-users`,
            this.classList.filter((classItem) => classItem.selected)
          )
          .then(() => {
            console.log('Task created and users assigned successfully');

            // Handle Closed Questions
            this.closedQuestions.forEach((closedQuestion) => {
              if (closedQuestion.file) {
                const fileDto = {
                  uploadedBy: this.authService.getUserWithoutToken(),
                  assignedClass: null,
                };

                // Create FormData for file upload
                const formData = new FormData();
                formData.append('file', closedQuestion.file);
                formData.append(
                  'fileDto',
                  new Blob([JSON.stringify(fileDto)], { type: 'application/json' })
                );

                // Upload the file
                this.axiosService
                  .uploadFileRequest('/files/upload', formData)
                  .then((response: { data: FileDto }) => {
                    const fileDtoResponse = {
                      ...response.data,
                      createdAt: parseDate(response.data.createdAt),
                    };

                    console.log('File uploaded successfully:', fileDtoResponse);

                    this.globalNotificationHandler.handleMessage(
                      'File uploaded successfully'
                    );

                    // Send the question payload with the uploaded file
                    this.sendClosedQuestion(closedQuestion, fileDtoResponse);
                  })
                  .catch((error) => {
                    console.error('Failed to upload file:', error);
                    this.globalNotificationHandler.handleError(error);
                  });
              } else {
                // Send the question payload without a file
                this.sendClosedQuestion(closedQuestion, null);
              }
            });

            // Handle Open Questions
            this.openQuestions.forEach((openQuestion) => {
              if (openQuestion.file) {
                const fileDto = {
                  uploadedBy: this.authService.getUserWithoutToken(),
                  assignedClass: null,
                };

                // Create FormData for file upload
                const formData = new FormData();
                formData.append('file', openQuestion.file);
                formData.append(
                  'fileDto',
                  new Blob([JSON.stringify(fileDto)], { type: 'application/json' })
                );

                // Upload the file
                this.axiosService
                  .uploadFileRequest('/files/upload', formData)
                  .then((response: { data: FileDto }) => {
                    const fileDtoResponse = {
                      ...response.data,
                      createdAt: parseDate(response.data.createdAt),
                    };

                    console.log('File uploaded successfully:', fileDtoResponse);

                    this.globalNotificationHandler.handleMessage(
                      'File uploaded successfully'
                    );

                    // Send the question payload with the uploaded file
                    this.sendOpenQuestion(openQuestion, fileDtoResponse);
                  })
                  .catch((error) => {
                    console.error('Failed to upload file:', error);
                    this.globalNotificationHandler.handleError(error);
                  });
              } else {
                // Send the question payload without a file
                this.sendOpenQuestion(openQuestion, null);
              }
            });

            // Handle Question IDs
            if (this.questionIdsFromBase.length > 0) {
              this.axiosService
                .request(
                  'POST',
                  `/questions/assignQuestionsToTask/${this.createdTask?.id}`,
                   this.questionIdsFromBase // Send the entire list of IDs in an array
                )
                .then(() =>
                  console.log('Questions assigned to the task successfully')
                )
                .catch((error: any) => {
                  this.globalNotificationHandler.handleError(error);
                  console.error('Failed to assign ready questions to the task:', error);
                });
            }
          })

          .catch((error: any) => {
            this.globalNotificationHandler.handleError(error);
            console.error('Failed to assign users to the task:', error);
          });
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to create task:', error);
    });
  }


// Helper function for sending closed question
  private sendClosedQuestion(closedQuestion: ClosedQuestion, fileDto: FileDto | null): void {
    const payload = [
      {
        question: {
          questionId: null,
          questionType: { questionTypeId: 1, typeName: 'Closed Question' },
          points: closedQuestion.points,
          content: closedQuestion.question,
          fileId: fileDto,
        },
        answers: Array.from(closedQuestion.answer).map(([content, isCorrect]) => ({
          content,
          isCorrect,
        })),
      },
    ];

    this.axiosService
      .request('POST', `/questions/withAnswers/${this.createdTask?.id}`, payload)
      .then(() =>
        console.log('Closed question with answers added successfully')
      )
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to add closed question:', error);
      });
  }

// Helper function for sending open question
  private sendOpenQuestion(openQuestion: OpenQuestion, fileDto: FileDto | null): void {
    const payload = [{
      question: {
        questionId: null,
        questionType: { questionTypeId: 2, typeName: 'Open Question' },
        points: openQuestion.points, // Adjust points as needed
        content: openQuestion.question,
        fileId: fileDto, // Attach the uploaded file if present
      },
      answers: [
        {
          content: openQuestion.answer,
          isCorrect: true, // Open question has a single correct answer
        },
      ],
    }];

    this.axiosService
      .request('POST', `/questions/withAnswers/${this.createdTask?.id}`, payload)
      .then(() =>
        console.log('Open question with answers added successfully')
      )
      .catch((error: any) => {
        this.globalNotificationHandler.handleError(error);
        console.error('Failed to add open question:', error);
      });
  }



  onClassSelected(classItem: ClassDto) {
    console.log('Selected class:', classItem);
  }

  toggleAddQuestionModal() {
    this.showAddQuestionModal = !this.showAddQuestionModal;
  }

  handleQuestionSelected(question: OpenQuestion | ClosedQuestion | number): void {
    if (typeof question === 'number') {
      if (!this.questionIdsFromBase.includes(question)) {

        this.axiosService.request('GET', `/questions/${question}`, {}).then(
          (response: { data: QuestionDto }) => {
            const questionDto = response.data;
            if (questionDto.questionType.typeName === 'Closed Question') {
              this.ReadyClosedQuestionsFromTheBase.push(questionDto);
            } else {
              this.ReadyOpenQuestionsFromTheBase.push(questionDto);
            }
          }
        ).catch((error: any) => {
          this.globalNotificationHandler.handleError(error);
          console.error('Failed to fetch classes:', error);
        });

        this.questionIdsFromBase.push(question);
        console.log('Ready Question ID:', question);
      } else {
        console.log('Duplicate Question ID skipped:', question);
      }
    } else if ('answer' in question && question.answer instanceof Map) {
      this.closedQuestions.push(question as ClosedQuestion);
      console.log('Closed Question:', question);
    } else {
      this.openQuestions.push(question as OpenQuestion);
      console.log('Open Question:', question);
    }
  }

  removeClosedQuestion(index: number): void {
    this.closedQuestions.splice(index, 1);
    console.log(`Removed closed question at index ${index}`);
  }

  removeOpenQuestion(index: number): void {
    this.openQuestions.splice(index, 1);
    console.log(`Removed open question at index ${index}`);
  }

  removeReadyClosedQuestion(index: number): void {
    const removedQuestionId = this.ReadyClosedQuestionsFromTheBase[index].questionId;
    this.ReadyClosedQuestionsFromTheBase.splice(index, 1);
    this.removeQuestionIdFromBase(removedQuestionId);
    console.log(`Removed closed question from base at index ${index}, ID: ${removedQuestionId}`);
  }

  removeReadyOpenQuestion(index: number): void {
    const removedQuestionId = this.ReadyOpenQuestionsFromTheBase[index].questionId;
    this.ReadyOpenQuestionsFromTheBase.splice(index, 1);
    this.removeQuestionIdFromBase(removedQuestionId);
    console.log(`Removed open question from base at index ${index}, ID: ${removedQuestionId}`);
  }

  private removeQuestionIdFromBase(questionId: number): void {
    const index = this.questionIdsFromBase.indexOf(questionId);
    if (index !== -1) {
      this.questionIdsFromBase.splice(index, 1);
      console.log(`Removed question ID ${questionId} from questionIdsFromBase.`);
    } else {
      console.warn(`Question ID ${questionId} not found in questionIdsFromBase.`);
    }
  }


  private calculateScore(): number {
    let score = 0;

    [
      this.closedQuestions,
      this.openQuestions,
      this.ReadyClosedQuestionsFromTheBase,
      this.ReadyOpenQuestionsFromTheBase
    ].forEach((questions) => {
      questions.forEach((q) => {
        score += q.points || 0; // Add points, default to 0 if undefined
      });
    });

    return score;
  }

}

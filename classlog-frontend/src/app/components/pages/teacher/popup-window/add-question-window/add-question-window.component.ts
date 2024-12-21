import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {UserDto} from "../../../../../model/entities/user-dto";
import {AxiosService} from "../../../../../service/axios/axios.service";
import {GlobalNotificationHandler} from "../../../../../service/notification/global-notification-handler.service";
import {ActivatedRoute} from "@angular/router";
import {ClosedQuestion, OpenQuestion} from "../../task-creator/task-creator.component";
import {QuestionDto} from "../../../../../model/entities/question-dto";

@Component({
  selector: 'app-add-question-window',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './add-question-window.component.html',
  styleUrl: './add-question-window.component.css'
})
export class AddQuestionWindowComponent {
  @Input() isOpen = false; // Modal visibility
  @Output() close = new EventEmitter<void>();

  activeTab: 'open' | 'close' | 'ready' = 'close'; // Current tab
  openQuestionText = ''; // Open question text
  closeQuestionText = ''; // Close question text
  closeOptions: string[] = ['']; // Options for close-ended questions
  correctOption: number | null = null; // Index of the correct option
  points: number | null = null;
  readyQuestions: QuestionDto[] = []; // Ready questions

  selectedFile: File | null = null; // File attachment
  openAnswer: string | null = null; // Open question answer

  readyQuestionId: number | null = null;
  @Output() questionSelected = new EventEmitter<OpenQuestion | ClosedQuestion | number>();


  constructor(
    private axiosService: AxiosService,
    private globalNotificationHandler: GlobalNotificationHandler,
    private route: ActivatedRoute
  ) {}

  setActiveTab(tab: 'open' | 'close' | 'ready'): void {
    this.activeTab = tab;
    this.points = null;
    if (this.readyQuestions.length === 0) {
      this.fetchReadyQuestions();
    }
  }

  onFileSelected(event: any, type: 'open' | 'close'): void {
    this.selectedFile = event.target.files[0];
    console.log(`${type} question file selected:`, this.selectedFile);
  }

  addCloseOption(): void {
    if (this.closeOptions.length < 4) {
      this.closeOptions.push('');
    }
  }

  trackByIndex(index: number, item: any): number {
    return index;
  }

  confirmSelection(): void {
    if (this.points === null || this.points <= 0) {
      this.globalNotificationHandler.handleMessage('Please enter a valid points value.');
      return;
    }

    let questionData: OpenQuestion | ClosedQuestion | number | undefined;

    if (this.activeTab === 'open') {
      questionData = {
        question: this.openQuestionText,
        answer: this.openAnswer || '',
        file: this.selectedFile || null,
        points: this.points
      } as OpenQuestion;
    } else if (this.activeTab === 'close') {
      const answerMap = new Map<string, boolean>();
      this.closeOptions.forEach((option, index) => {
        answerMap.set(option, index === this.correctOption);
      });

      questionData = {
        question: this.closeQuestionText,
        answer: answerMap,
        file: this.selectedFile || null,
        points: this.points
      } as ClosedQuestion;
    } else if (this.activeTab === 'ready') {
      questionData = this.readyQuestionId ?? undefined;
    }

    if (questionData !== undefined) {
      this.questionSelected.emit(questionData);
    }

    this.close.emit();
  }


  removeCloseOption(index: number): void {
    this.closeOptions.splice(index, 1);

    // Adjust the correctOption if the removed option affects it
    if (this.correctOption === index) {
      this.correctOption = null;
    } else if (this.correctOption !== null && this.correctOption > index) {
      this.correctOption -= 1;
    }
  }

  closeWindow(): void {
    this.close.emit();
  }

  private fetchReadyQuestions() {
    this.axiosService.request('GET', '/questions', {}).then(
      (response: { data: QuestionDto[] }) => {
        this.readyQuestions = response.data;
        console.log('Fetched ready questions:', this.readyQuestions);
      }
    ).catch((error: any) => {
      this.globalNotificationHandler.handleError(error);
      console.error('Failed to fetch ready questions:', error);
  });
}

  selectReadyQuestion(question: QuestionDto): void {
    this.readyQuestionId = question.questionId; // Set the selected question ID
    console.log('Selected Ready Question:', question);
  }

}

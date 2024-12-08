import { Component } from '@angular/core';
import {HeaderComponent} from "../../../shared/header/header.component";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-task-details',
  standalone: true,
  imports: [
    HeaderComponent,
    NgForOf
  ],
  templateUrl: './task-details.component.html',
  styleUrl: './task-details.component.css'
})
export class TaskDetailsComponent {
  questions = [
    {
      number: 1,
      content: 'What is the capital of France?'
    },
    {
      number: 2,
      content: 'Solve the equation: 3x + 5 = 11. What is the value of x?'
    },
    {
      number: 3,
      content: 'Explain the process of photosynthesis in plants.'
    },
    {
      number: 4,
      content: 'What is the largest planet in our solar system?'
    },
    {
      number: 5,
      content: 'Describe the water cycle and its main stages.'
    },
    {
      number: 6,
      content: 'Translate the following sentence to Spanish: "The cat is sleeping under the table."'
    }
  ];
}

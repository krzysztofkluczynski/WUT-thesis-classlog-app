import { Component, Input } from '@angular/core';
import {UserDto} from "../../../model/entities/user-dto";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-class-tile',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './class-tile.component.html',
  styleUrls: ['./class-tile.component.css'] // Corrected to "styleUrls"
})
export class ClassTileComponent {
  @Input() title: string | undefined = 'Class Title';
  @Input() description: string | undefined = 'Class Description';
  @Input() teachers: UserDto[] = [];
}

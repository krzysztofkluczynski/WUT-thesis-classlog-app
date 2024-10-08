import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-class-tile',
  standalone: true,
  imports: [],
  templateUrl: './class-tile.component.html',
  styleUrl: './class-tile.component.css'
})
export class ClassTileComponent {
  @Input() title: string = 'Class Title';
  @Input() description: string = 'Class Description';
  @Input() teacher: string = 'Teacher Name';
}

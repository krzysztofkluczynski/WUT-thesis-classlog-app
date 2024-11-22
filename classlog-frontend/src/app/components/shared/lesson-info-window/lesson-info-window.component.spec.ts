import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LessonInfoWindowComponent } from './lesson-info-window.component';

describe('LessonInfoWindowComponent', () => {
  let component: LessonInfoWindowComponent;
  let fixture: ComponentFixture<LessonInfoWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LessonInfoWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LessonInfoWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

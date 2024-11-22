import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LessonPopupComponent } from './lesson-popup.component';

describe('LessonPopupComponent', () => {
  let component: LessonPopupComponent;
  let fixture: ComponentFixture<LessonPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LessonPopupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LessonPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

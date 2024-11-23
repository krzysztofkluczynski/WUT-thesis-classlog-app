import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddQuestionWindowComponent } from './add-question-window.component';

describe('AddQuestionWindowComponent', () => {
  let component: AddQuestionWindowComponent;
  let fixture: ComponentFixture<AddQuestionWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddQuestionWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddQuestionWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

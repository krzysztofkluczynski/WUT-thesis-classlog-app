import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewGradeWindowComponent } from './new-grade-window.component';

describe('NewGradeWindowComponent', () => {
  let component: NewGradeWindowComponent;
  let fixture: ComponentFixture<NewGradeWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewGradeWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewGradeWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

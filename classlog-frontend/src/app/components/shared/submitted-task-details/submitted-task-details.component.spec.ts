import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTaskDetailsComponent } from './submitted-task-details.component';

describe('SubmittedTaskDetailsComponent', () => {
  let component: SubmittedTaskDetailsComponent;
  let fixture: ComponentFixture<SubmittedTaskDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubmittedTaskDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubmittedTaskDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

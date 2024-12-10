import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskResolverComponent } from './task-resolver.component';

describe('TaskResolverComponent', () => {
  let component: TaskResolverComponent;
  let fixture: ComponentFixture<TaskResolverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskResolverComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaskResolverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

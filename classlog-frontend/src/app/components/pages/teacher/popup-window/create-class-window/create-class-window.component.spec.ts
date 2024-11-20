import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateClassWindowComponent } from './create-class-window.component';

describe('CreateClassWindowComponent', () => {
  let component: CreateClassWindowComponent;
  let fixture: ComponentFixture<CreateClassWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateClassWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

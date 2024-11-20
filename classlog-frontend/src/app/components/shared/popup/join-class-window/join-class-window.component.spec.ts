import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JoinClassWindowComponent } from './join-class-window.component';

describe('JoinClassWindowComponent', () => {
  let component: JoinClassWindowComponent;
  let fixture: ComponentFixture<JoinClassWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JoinClassWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JoinClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowClassCodeWindowComponent } from './show-class-code-window.component';

describe('ShowClassCodeWindowComponent', () => {
  let component: ShowClassCodeWindowComponent;
  let fixture: ComponentFixture<ShowClassCodeWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowClassCodeWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowClassCodeWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

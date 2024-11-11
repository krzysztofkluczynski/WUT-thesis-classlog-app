import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUserToClassWindowComponent } from './add-user-to-class-window.component';

describe('AddUserToClassWindowComponent', () => {
  let component: AddUserToClassWindowComponent;
  let fixture: ComponentFixture<AddUserToClassWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddUserToClassWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddUserToClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

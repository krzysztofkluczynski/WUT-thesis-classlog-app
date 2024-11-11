import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteUserFromClassWindowComponent } from './delete-user-from-class-window.component';

describe('DeleteUserFromClassWindowComponent', () => {
  let component: DeleteUserFromClassWindowComponent;
  let fixture: ComponentFixture<DeleteUserFromClassWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteUserFromClassWindowComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteUserFromClassWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

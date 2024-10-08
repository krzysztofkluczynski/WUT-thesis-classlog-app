import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassTileComponent } from './class-tile.component';

describe('ClassTileComponent', () => {
  let component: ClassTileComponent;
  let fixture: ComponentFixture<ClassTileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassTileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClassTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnknownDashboardComponent } from './unknown-dashboard.component';

describe('UnknownDashboardComponent', () => {
  let component: UnknownDashboardComponent;
  let fixture: ComponentFixture<UnknownDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UnknownDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnknownDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

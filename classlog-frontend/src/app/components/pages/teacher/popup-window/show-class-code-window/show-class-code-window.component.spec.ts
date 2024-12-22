import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ShowClassCodeWindowComponent } from './show-class-code-window.component';
import { ClassDto } from '../../../../../model/entities/class-dto';
import { By } from '@angular/platform-browser';

describe('ShowClassCodeWindowComponent', () => {
  let component: ShowClassCodeWindowComponent;
  let fixture: ComponentFixture<ShowClassCodeWindowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowClassCodeWindowComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ShowClassCodeWindowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should emit close event when closeWindow is called', () => {
    spyOn(component.close, 'emit');

    component.closeWindow();

    expect(component.close.emit).toHaveBeenCalled();
  });

  it('should emit close event when confirmSelection is called', () => {
    spyOn(component.close, 'emit');

    component.confirmSelection();

    expect(component.close.emit).toHaveBeenCalled();
  });

  it('should not display code if classDto is null', () => {
    component.classDto = null;
    component.isOpen = true;

    fixture.detectChanges();

    const codeElement = fixture.debugElement.query(By.css('.class-code'));
    expect(codeElement).toBeNull();
  });
});

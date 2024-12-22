import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClassTileComponent } from './class-tile.component';
import { UserDto } from '../../../model/entities/user-dto';
import { By } from '@angular/platform-browser';

describe('ClassTileComponent', () => {
  let component: ClassTileComponent;
  let fixture: ComponentFixture<ClassTileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClassTileComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ClassTileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should display the provided title and description', () => {
    component.title = 'Test Class Title';
    component.description = 'Test Class Description';
    fixture.detectChanges();

    const titleElement = fixture.debugElement.query(By.css('.class-title')).nativeElement;
    const descriptionElement = fixture.debugElement.query(By.css('.class-description')).nativeElement;

    expect(titleElement.textContent).toContain('Test Class Title');
    expect(descriptionElement.textContent).toContain('Test Class Description');
  });

  it('should display "Class Title" and "Class Description" if no inputs are provided', () => {
    fixture.detectChanges();

    const titleElement = fixture.debugElement.query(By.css('.class-title')).nativeElement;
    const descriptionElement = fixture.debugElement.query(By.css('.class-description')).nativeElement;

    expect(titleElement.textContent).toContain('Class Title');
    expect(descriptionElement.textContent).toContain('Class Description');
  });

  it('should display a list of teacher names if provided', () => {
    component.teachers = [
      { id: 1, name: 'John', surname: 'Doe', email: '', role: { id: 1, roleName: 'Teacher' }, token: '', createdAt: new Date() },
      { id: 2, name: 'Jane', surname: 'Smith', email: '', role: { id: 1, roleName: 'Teacher' }, token: '', createdAt: new Date() },
    ];
    fixture.detectChanges();

    const teacherElements = fixture.debugElement.queryAll(By.css('.class-teacher'));
    expect(teacherElements.length).toBe(2);
    expect(teacherElements[0].nativeElement.textContent.trim()).toBe('John Doe');
    expect(teacherElements[1].nativeElement.textContent.trim()).toBe('Jane Smith');
  });

  it('should display "Teachers:" text only if there are teachers provided', () => {
    component.teachers = [];
    fixture.detectChanges();

    component.teachers = [
      { id: 1, name: 'John', surname: 'Doe', email: '', role: { id: 1, roleName: 'Teacher' }, token: '', createdAt: new Date() },
    ];
    fixture.detectChanges();

    const updatedTeacherTextElement = fixture.debugElement.query(By.css('.teacher-text')).nativeElement;
    expect(updatedTeacherTextElement.textContent.trim()).toBe('Teachers:');
  });

  it('should display no teacher names if the teacher list is empty', () => {
    component.teachers = [];
    fixture.detectChanges();

    const teacherElements = fixture.debugElement.queryAll(By.css('.class-teacher'));
    expect(teacherElements.length).toBe(0);
  });
});

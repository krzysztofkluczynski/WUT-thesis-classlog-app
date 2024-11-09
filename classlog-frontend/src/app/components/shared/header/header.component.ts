import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgClass, NgIf } from "@angular/common";
import { AuthService } from "../../../service/auth/auth.service";
import { Router, NavigationEnd } from "@angular/router";
import { filter } from 'rxjs/operators';
import { HeaderOptions } from "../../../utils/header-options";
import { HeaderService } from "../../../service/header/header.service";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgIf,
    NgClass,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  @Input() pageTitle: string = '';
  @Input() ifShowMenu: boolean = false;

  logoPath: string = 'assets/images/classlog_logo_yellow_cut.png';
  roleId: number | undefined;
  selectedOption: HeaderOptions | null = HeaderOptions.Students;

  constructor(private router: Router, public authService: AuthService, private headerService: HeaderService) {}

  setSelectedOption(option: HeaderOptions): void {
    this.headerService.setSelectedOption(option);
    this.redirectBasedOnOption(option);
  }

  redirectBasedOnOption(option: HeaderOptions): void {
    this.selectedOption = this.headerService.getSelectedOption();
    switch (option) {
      case HeaderOptions.ClassesTeacher:
        this.router.navigate(['/teacher/dashboard']);
        break;
      case HeaderOptions.TasksTeacher:
        this.router.navigate(['/teacher/tasks']);
        break;
      case HeaderOptions.GradesTeacher:
        this.router.navigate(['/teacher/grades']);
        break;
      case HeaderOptions.ClassesStudent:
        this.router.navigate(['/student/classes']);
        break;
      case HeaderOptions.TasksStudent:
        this.router.navigate(['/student/tasks']);
        break;
      case HeaderOptions.GradesStudent:
        this.router.navigate(['/student/grades']);
        break;
      case HeaderOptions.Students:
        this.router.navigate(['/admin/students']);
        break;
      case HeaderOptions.Teachers:
        this.router.navigate(['/admin/teachers']);
        break;
      case HeaderOptions.Admins:
        this.router.navigate(['/admin/admins']);
        break;
      case HeaderOptions.Unassigned:
        this.router.navigate(['/admin/unassigned']);
        break;
      default:
        break;
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  ngOnInit(): void {
    this.roleId = this.authService.getUser()?.role.id;

    this.headerService.selectedOption$.subscribe(option => {
      this.selectedOption = option;
    }); //Whenever selectedOption$ emits a new value (via HeaderService), it updates the selectedOption property in the component.

    this.router.events.pipe( //Updates the header state (selectedOption) based on the current URL whenever the route changes.
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      const url = event.urlAfterRedirects;
      const pathToHeaderOptionMap = new Map<string, HeaderOptions>([
        ['/teacher/dashboard', HeaderOptions.ClassesTeacher],
        ['/teacher/tasks', HeaderOptions.TasksTeacher],
        ['/teacher/grades', HeaderOptions.GradesTeacher],
        ['/student/classes', HeaderOptions.ClassesStudent],
        ['/student/tasks', HeaderOptions.TasksStudent],
        ['/student/grades', HeaderOptions.GradesStudent],
        ['/admin/students', HeaderOptions.Students],
        ['/admin/teachers', HeaderOptions.Teachers],
        ['/admin/admins', HeaderOptions.Admins],
        ['/admin/unassigned', HeaderOptions.Unassigned],
      ]);

      const foundOption = Array.from(pathToHeaderOptionMap.entries()).find(([path, option]) => url.includes(path));
      if (foundOption) {
        this.headerService.setSelectedOption(foundOption[1]);
      }
    });
  }
  protected readonly HeaderOptions = HeaderOptions;

  goToProfile() {
    this.router.navigate(['/profile', this.authService.getUser()?.id], { queryParams: { editMode: true } });
  }
}

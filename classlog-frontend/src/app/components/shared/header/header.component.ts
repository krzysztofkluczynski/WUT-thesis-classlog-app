import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgClass, NgIf} from "@angular/common";
import {AuthService} from "../../../service/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgIf,
    NgClass
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  @Input() pageTitle: string = '';
  @Input() ifShowMenu: boolean = false;
  @Output() optionSelected = new EventEmitter<string>();


  logoPath: string = 'assets/images/classlog_logo_yellow_cut.png';
  roleId: number | undefined;
  selectedOption: string = '';

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  setSelectedOption(option: string): void {
    this.selectedOption = option;
    this.optionSelected.emit(option);
    this.router.navigate(['/admin-dashboard']);
  }

  ngOnInit(): void {
    this.roleId = this.authService.getUser()?.role.id;

    if (this.roleId === 1) {
      this.selectedOption = 'ClassesTeacher';
    } else if (this.roleId === 2) {
      this.selectedOption = 'ClassesStudent';
    } else if (this.roleId === 3) {
      this.selectedOption = 'Students';
    }
  }
}

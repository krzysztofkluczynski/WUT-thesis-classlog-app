import {Component, Input} from '@angular/core';
import {NgClass, NgIf, NgOptimizedImage} from "@angular/common";
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
export class HeaderComponent {
  @Input() pageTitle: string = '';
  @Input() ifShowMenu: boolean = false;
  userId: number | undefined;

  logoPath: string = 'assets/images/classlog_logo_yellow_cut.png';

  constructor(
    public authService: AuthService,
    private router: Router
  ) {
    this.userId = this.authService.getUser()?.id;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

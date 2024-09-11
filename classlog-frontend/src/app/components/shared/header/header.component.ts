import {Component, Input} from '@angular/core';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {AuthService} from "../../../service/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Input() pageTitle: string = '';
  @Input() logoPath: string = '';
  @Input() ifShowMenu: boolean = false;

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

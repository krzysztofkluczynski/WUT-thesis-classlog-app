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
  @Input() pageTitle: string = ''; // Initialize with default value
  @Input() logoPath: string = '';  // Initialize with default value
  @Input() ifShowMenu: boolean = false;  // Initialize with default value

  constructor(
    public authService: AuthService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.setAuthToken(null);
    this.authService.setUserRole(null);
    this.router.navigate(['/login']);
  }
}

import { Routes } from '@angular/router';
import { LoginFormComponent } from './components/pages/starting-page/login-form/login-form.component';
import {AdminDashboardComponent} from "./components/pages/admin-dashboard/admin-dashboard.component";
import {StudentDashboardComponent} from "./components/pages/student-dashboard/student-dashboard.component";
import {TeacherDashboardComponent} from "./components/pages/teacher-dashboard/teacher-dashboard.component";
import {authGuard} from "./guard/auth.guard";
export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginFormComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent, canActivate: [authGuard], data: { role: 'admin' } },
  { path: 'user-dashboard', component: StudentDashboardComponent, canActivate: [authGuard], data: { role: 'student' } },
  { path: 'teacher-dashboard', component: TeacherDashboardComponent, canActivate: [authGuard], data: { role: 'teacher' } }

];

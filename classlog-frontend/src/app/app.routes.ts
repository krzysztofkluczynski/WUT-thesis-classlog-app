import { Routes } from '@angular/router';
import { LoginFormComponent } from './components/pages/login-form/login-form.component';
import {AdminDashboardComponent} from "./components/pages/admin/admin-dashboard/admin-dashboard.component";
import {StudentDashboardComponent} from "./components/pages/student/student-dashboard/student-dashboard.component";
import {TeacherDashboardComponent} from "./components/pages/teacher/teacher-dashboard/teacher-dashboard.component";
import {authGuard} from "./guard/auth.guard";
import {RegisterFormComponent} from "./components/pages/register-form/register-form.component";
import {UnknownDashboardComponent} from "./components/pages/unknown-dashboard/unknown-dashboard.component";
import {UserProfileComponent} from "./components/shared/user-profile/user-profile.component";
export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegisterFormComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent, canActivate: [authGuard], data: { role: 'Admin' } },
  { path: 'user-profile/:id', component: UserProfileComponent, canActivate: [authGuard] },
  { path: 'user-dashboard', component: StudentDashboardComponent, canActivate: [authGuard], data: { role: 'Student' } },
  { path: 'teacher-dashboard', component: TeacherDashboardComponent, canActivate: [authGuard], data: { role: 'Teacher' } },
  {path: 'unknown-dashboard', component: UnknownDashboardComponent, canActivate: [authGuard], data: { role: 'Unknown'} }
];

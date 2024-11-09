import { Routes } from '@angular/router';
import { LoginFormComponent } from './components/pages/login-form/login-form.component';
import { AdminDashboardComponent } from './components/pages/admin/admin-dashboard/admin-dashboard.component';
import { StudentDashboardComponent } from './components/pages/student/student-dashboard/student-dashboard.component';
import { TeacherDashboardComponent } from './components/pages/teacher/teacher-dashboard/teacher-dashboard.component';
import { authGuard } from './guard/auth.guard';
import { RegisterFormComponent } from './components/pages/register-form/register-form.component';
import { UnknownDashboardComponent } from './components/pages/unknown-dashboard/unknown-dashboard.component';
import { UserProfileComponent } from './components/shared/user-profile/user-profile.component';
import { StudentClassComponent } from './components/pages/student/student-class/student-class.component';
import { StudentGradesComponent } from './components/pages/student/student-grades/student-grades.component';
import { StudentTasksComponent } from './components/pages/student/student-tasks/student-tasks.component';
import { TeacherClassComponent } from './components/pages/teacher/teacher-class/teacher-class.component';
import { TeacherTasksComponent } from './components/pages/teacher/teacher-tasks/teacher-tasks.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginFormComponent },
  { path: 'register', component: RegisterFormComponent },
  {
    path: 'admin',
    canActivate: [authGuard],
    data: { role: 'Admin' },
    children: [
      { path: ':section', component: AdminDashboardComponent },
    ],
  },
  {
    path: 'student',
    canActivate: [authGuard],
    data: { role: 'Student' },
    children: [
      { path: 'dashboard', component: StudentDashboardComponent },
      { path: 'classes', component: StudentClassComponent },
      { path: 'grades', component: StudentGradesComponent },
      { path: 'tasks', component: StudentTasksComponent },
    ],
  },
  {
    path: 'teacher',
    canActivate: [authGuard],
    data: { role: 'Teacher' },
    children: [
      { path: 'dashboard', component: TeacherDashboardComponent },
      { path: 'class', component: TeacherClassComponent },
      { path: 'grades', component: TeacherClassComponent }, // Same component for now, can be changed later
      { path: 'tasks', component: TeacherTasksComponent },
    ],
  },
  {
    path: 'profile/:id',
    component: UserProfileComponent,
    canActivate: [authGuard]
  }, // Only route with `id` in the path
  {
    path: 'unknown',
    component: UnknownDashboardComponent,
    canActivate: [authGuard],
    data: { role: 'Unknown' }
  }, // Role restriction added for Unknown
  { path: '**', redirectTo: '/login' }, // Fallback for invalid routes
];

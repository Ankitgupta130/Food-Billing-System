import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
// import { EmployeeDashboardComponent } from './employee/employee-dashboard/employee-dashboard.component';
import { authGuard } from './guards/auth.guard';
import { adminRoutes } from './admin/admin.routes';
import { EmployeeLayoutComponent } from './employee/employee-layout/employee-layout.component';
import { employeeRoutes } from './employee/employee.routes';

export const routes: Routes = [

    {
        path: '', redirectTo:'login', pathMatch:'full'

    },

    {
        path: 'login',
        loadComponent: ()=> import('./pages/login/login.component').then(m=>m.LoginComponent)
    },

   {
    path: 'admin',
    canActivate: [authGuard],
    data: {role:'ADMIN'},
    children:adminRoutes
   },
  
   {
    path: 'employee',
    canActivate: [authGuard],
    data: {role: 'EMPLOYEE'},
    children: employeeRoutes
   },

    {
    path:'**',
    redirectTo:'login'
   },
  
];

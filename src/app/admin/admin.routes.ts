import { Routes } from "@angular/router";
import { AdminLayoutComponent } from "./admin-layout/admin-layout.component";
import { AdminDashboardComponent } from "./pages/dashboard/admin-dashboard/admin-dashboard.component";
import { authGuard } from "../guards/auth.guard";

export const adminRoutes: Routes = [
    {
        path: '',
        component: AdminLayoutComponent,
        canActivateChild:[authGuard],
        data:{role:'ADMIN'},
        children: [
            {
                path:'dashboard',
                loadComponent: ()=>import('./pages/dashboard/admin-dashboard/admin-dashboard.component').then(m=>m.AdminDashboardComponent)
            },

            {
                path:'employees',
                loadComponent: ()=> import('./pages/employees/employee-list/employee-list.component').then(m=>m.EmployeeListComponent)
            },

            {
                path:'food-item',
                loadComponent: ()=> import('./pages/food-item/food-item.component').then(m=>m.FoodItemComponent)
            },

            {
                path:'reports',
                loadComponent: ()=>import('./pages/reports/reports.component').then(m=>m.ReportsComponent)
            },

            {
                path:'settings',
                loadComponent: ()=>import('./pages/settings/settings.component').then(m=>m.SettingsComponent)
            },
            {
                path:'',
                redirectTo:'dashboard',
                pathMatch:'full'
            }
        ]
    }
]

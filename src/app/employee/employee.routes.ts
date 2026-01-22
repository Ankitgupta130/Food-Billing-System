import { Routes } from "@angular/router";
import { EmployeeLayoutComponent } from "./employee-layout/employee-layout.component";
import { authGuard } from "../guards/auth.guard";

export const employeeRoutes: Routes = [
    {
        path: '',
        component: EmployeeLayoutComponent,
        canActivateChild:[authGuard],
        data: {role:'EMPLOYEE'},
        children: [
            {
                path: 'dashboard',
                loadComponent: () => import("../employee/pages/dashboard/dashboard.component").then(m => m.DashboardComponent)
            },

            {
                path: 'cart',
                loadComponent: ()=> import("../employee/pages/cart/cart.component").then(m=>m.CartComponent)
            },

            {
                path: 'orderhistory',
                loadComponent: ()=> import("../employee/pages/order-history/order-history.component").then(m=>m.OrderHistoryComponent)
            },

            {
                path: '',
                redirectTo: 'dashboard',
                pathMatch: 'full'
            }
        ]
    }



]
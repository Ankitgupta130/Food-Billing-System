import { inject } from '@angular/core';
import { CanActivateChildFn, CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn & CanActivateChildFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const user = authService.getUserData();

  // No user? Redirect to login
  if (!user) {
    router.navigate(['/login']);
    return false;
  }



  const expectedRole = route.data?.['role'];

  // If a role is required and doesn't match, redirect
  if (expectedRole && user.role !== expectedRole) {
    router.navigate(['/login']);
    return false;
  }

  return true;
};

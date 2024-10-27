import { Injectable, Injector } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private injector: Injector, private jwtHelper: JwtHelperService) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const router = this.injector.get(Router);
    const authToken = localStorage.getItem('authToken');
    const userRole = localStorage.getItem('userRole');
    
    console.log('Ruolo attuale salvato:', userRole); 
    
    if (authToken && !this.jwtHelper.isTokenExpired(authToken)) {
      if (route.data['role'] && route.data['role'] !== userRole) {
        router.navigate(['/']);
        return false;
      }
      
      if (userRole === 'ROLE_ADMIN' && route.url[0].path !== 'admin-dashboard') {
        router.navigate(['/admin-dashboard']);
        return false;
      }
    
      return true;
    } else {
      localStorage.removeItem('authToken');
      localStorage.removeItem('userRole');
      router.navigate(['/login']);
      return false;
    }
  }
  
}
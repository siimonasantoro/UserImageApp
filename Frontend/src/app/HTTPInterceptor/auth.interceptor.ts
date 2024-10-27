import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('authToken');
  
    if (token) {
      console.log('Aggiungendo token alle intestazioni:', token); // Debug
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
  
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error during HTTP request:', error);
        if (error.status === 401) {
          localStorage.removeItem('authToken');
          localStorage.removeItem('userRole');
          this.router.navigate(['/login']);
        } else if (error.status === 403) {
          console.error('Access denied - insufficient permissions');
        }
        return throwError(error);
      })
    );
  }
}  
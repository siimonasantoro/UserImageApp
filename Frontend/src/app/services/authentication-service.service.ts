import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { ResponseDTO } from '../model/responseDTO';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private apiUrl = 'http://localhost:8080/api/public/sign-in';  

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    return this.http.post<any>(this.apiUrl, { username, password }).pipe(
        tap(response => {
            if (response.status && response.content) {
                const { token, userId, role } = response.content;
                this.saveUserData(token, userId.toString(), role);
            } else {
                console.error('Token mancante nella risposta.', response);
            }
        })
    );
}

private saveUserData(token: string, userId: string, userRole: string) {
  localStorage.setItem('authToken', token);
  localStorage.setItem('userId', userId);
  localStorage.setItem('userRole', userRole); 
}

getCurrentUser() {
  const user = localStorage.getItem('authToken'); 
  return user ? JSON.parse(user) : null; 
}
  saveToken(token: string) {
    localStorage.setItem('authToken', token);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  logout() {
    localStorage.removeItem('authToken');
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

  
}

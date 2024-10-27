import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})


  export class LoginComponent {
    username: string = '';
    password: string = '';
    errorMessage: string = ''; 
  
    constructor(private http: HttpClient, private router: Router) {}
  
    goToRegister() {
      this.router.navigate(['/register']);
    }
    onSubmit() {
        this.errorMessage = '';
    
        if (!this.username || !this.password) {
          this.errorMessage = 'Username e password sono obbligatori';
          return;
        }
    
        const loginData = { username: this.username, password: this.password };
    
        this.http.post<any>('http://localhost:8080/api/public/sign-in', loginData).subscribe(
          response => {
            console.log('Risposta del login:', response);
    
            const token = response.token;
            const userId = response?.content?.userId;
            const roles: string[] = response?.content?.userRole || [];
    
            console.log('Ruoli restituiti:', roles);
    
            if (token) {
              localStorage.setItem('authToken', token);
              localStorage.setItem('userId', userId ? userId.toString() : '');
    
              if (roles.includes('ROLE_ADMIN')) {
                localStorage.setItem('userRole', 'ROLE_ADMIN');
                console.log('Utente con ruolo ADMIN trovato.');
                this.router.navigate(['/admin-dashboard']); 
              } else if (roles.length > 0) {
                const userRole = roles[0]; 
                localStorage.setItem('userRole', userRole);
                console.log('Ruolo salvato nel localStorage:', userRole);
                this.router.navigate(['/dashboard']);
              } else {
                localStorage.setItem('userRole', 'ROLE_USER');
                this.router.navigate(['/dashboard']);
              }
            } else {
              this.errorMessage = 'Token mancante nella risposta.';
            }
          },
          error => {
            if (error.status === 401) {
              this.errorMessage = 'Username o password errati';
            } else {
              this.errorMessage = 'Errore durante il login';
            }
            console.error('Errore durante il login', error);
          }
        );
      }
    
        }      
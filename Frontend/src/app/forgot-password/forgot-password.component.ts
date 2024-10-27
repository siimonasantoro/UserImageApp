import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  email: string = '';
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {} // Inietta ActivatedRoute

  onSubmit(form: NgForm) {
    const emailData = { email: this.email };

    this.http.post('http://localhost:8080/api/public/forgot-password', emailData).subscribe(
      response => {
        this.successMessage = 'Controlla la tua email per le istruzioni di recupero della password.';
        this.errorMessage = ''; 
      },
      error => {
        this.errorMessage = 'Si è verificato un errore. Assicurati che l\'email sia corretta.';
        this.successMessage = ''; 
      }
    );
  }
  onSubmitResetPassword(form: NgForm) {
    const authToken = this.route.snapshot.queryParamMap.get('authToken'); 
    const newPassword = form.value.password; 

    this.http.post(`http://localhost:8080/api/public/reset-password`, { 
        token: authToken, 
        newPassword: newPassword 
    })
    .subscribe(
        response => {
            console.log('Password reset avvenuto con successo');
            this.router.navigate(['/login']); 
        },
        error => {
            console.error('Errore nel reset della password', error);
          
        }
    );
}
}
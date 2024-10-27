import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { User } from '../model/user.model';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  successMessage: string = '';
  errorMessage: string = ''; 

  constructor(private http: HttpClient, private router: Router) {}

  onSubmit(form: NgForm) {
    const registrationData: User = {
      id: 0,
      username: this.username,
      email: this.email,
      password: this.password
    };

    const emailPattern = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
    if (!emailPattern.test(this.email)) {
      this.errorMessage = 'Email non valida'; 
      return; 
    }

    this.http.post('http://localhost:8080/api/public/sign-up', registrationData).subscribe(
      response => {
        this.successMessage = 'Registrazione avvenuta con successo!';
        this.errorMessage = ''; 
        console.log('Registrazione riuscita!', response);
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000); 
      },
      error => {
        this.errorMessage = 'Errore durante la registrazione. Riprovare.';
        console.error('Errore durante la registrazione', error);
        this.successMessage = '';
      }
    );
  }
  
}

import { Component } from '@angular/core';
import { Router } from '@angular/router'; // Importa Router

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  constructor(private router: Router) { } 

  goToRegister() {
    this.router.navigate(['/register']);
  }

}

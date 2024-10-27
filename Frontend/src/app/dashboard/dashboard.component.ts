import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/services/image.service';
import { AuthenticationService } from '../services/authentication-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit { 
  images: any[] = [];
  selectedFile: File | null = null;
  searchTerm: string = '';
  statusMessage: string = ''; 

  constructor(private imageService: ImageService, private authenticationService: AuthenticationService, private router: Router) {}

  ngOnInit(): void {
    this.loadImages();
  }

  loadImages() {
    this.imageService.getAllImages().subscribe(data => {
      console.log('Risposta API:', data); 
      if (Array.isArray(data)) {
        this.images = data.map((image: any) => ({
          ...image,
          url: `data:${image.contentType};base64,${image.data}`
        }));
        console.log('Immagini caricate:', this.images);
      } else {
        console.error('Dati ricevuti non sono un array:', data);
      }
    }, error => {
      console.error('Errore durante il recupero delle immagini:', error);
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']); 
  }

  uploadImage() {
    if (!this.selectedFile) {
      console.error('Nessun file selezionato.');
      return;
    }

    const formData = new FormData();
    formData.append('image', this.selectedFile);

    const userId = localStorage.getItem('userId');
    if (userId) {
      this.imageService.uploadImage(userId, formData).subscribe(
        response => {
          console.log('Immagine caricata con successo!', response);
          this.loadImages(); 
          this.statusMessage = 'Immagine caricata con successo!'; 
          this.selectedFile = null; 
          setTimeout(() => this.statusMessage = '', 3000);
        },
        error => {
          console.error('Errore durante il caricamento dell\'immagine', error);
          this.statusMessage = 'Errore durante il caricamento dell\'immagine.'; 
          setTimeout(() => this.statusMessage = '', 3000);
        }
      );
    } else {
      console.error('ID utente non trovato nel localStorage.');
    }
  }

  onFileSelected(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length) {
      this.selectedFile = target.files[0];
      console.log('File selezionato:', this.selectedFile);
    }
  }

  deleteImage(imageId: number): void {
    this.imageService.deleteImage(imageId).subscribe(() => {
      this.images = this.images.filter(image => image.id !== imageId);
      this.statusMessage = 'Immagine eliminata con successo!'; 
      setTimeout(() => this.statusMessage = '', 3000); 
    }, error => {
      console.error('Errore durante l\'eliminazione dell\'immagine:', error);
      this.statusMessage = 'Errore durante l\'eliminazione dell\'immagine.';
      setTimeout(() => this.statusMessage = '', 3000); 
    });
  }

  get filteredImages() {
    return this.images.filter(image => 
      image.filename && image.filename.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }
}

import { Component, OnInit } from '@angular/core';
import { ImageService } from '../services/image.service';
import { AdminService } from '../services/admin.service';
import { User } from '../model/user.model';
import { Image } from '../model/image.model';
import { Router } from '@angular/router';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog'; 
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  users: User[] = [];
  selectedUserId: number | null = null; 
  images: Image[] = []; 
  searchTerm: string = ''; 
  selectedFile: File | null = null; 
  selectedUserImages: any[] = [];


  constructor(private adminService: AdminService, private imageService: ImageService, private router: Router, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.imageService.getAllUsers().subscribe(data => {
      this.users = data;
      console.log('Utenti caricati:', this.users);
    }, error => {
      console.error('Errore durante il recupero degli utenti:', error);
    });
  }

  loadUserImages(userId: number) {
    this.imageService.getUserImages(userId).subscribe(data => {
      this.selectedUserImages = data.map((image: any) => ({
        ...image,
        url: `data:${image.contentType};base64,${image.data}`
      }));
      console.log(`Immagini per l'utente ${userId}:`, this.selectedUserImages);
    }, error => {
      console.error('Errore durante il recupero delle immagini:', error);
    });
  }

  
  deleteUser(userId: number) {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('authToken')}`
  });
    const dialogRef = this.dialog.open(ConfirmDialogComponent);
  
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.adminService.deleteUser(userId).subscribe(
          () => {
            this.users = this.users.filter(user => user.id !== userId);
            console.log(`Utente ${userId} eliminato con successo`);
          },
          error => {
            console.error('Errore durante l\'eliminazione dell\'utente:', error);
            alert('Errore durante l\'eliminazione dell\'utente. Riprova.');
          }
        );
      }
    });
}

  logout() {
    this.router.navigate(['/login']);
  }
}
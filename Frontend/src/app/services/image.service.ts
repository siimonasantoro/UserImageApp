import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import { Image } from '../model/image.model';
import { ResponseDTO } from '../model/responseDTO';

@Injectable({
  providedIn: 'root'
})
export class ImageService { 
  private baseUrl = 'http://localhost:8080/images';
  private userBaseUrl = 'http://localhost:8080/users'; 
  private apiUrl = 'http://localhost:8080/api/admin';
  constructor(private http: HttpClient) { }

  getAuthHeaders() {
    const token = localStorage.getItem('authToken'); 
    return {
        Authorization: `Bearer ${token}`
    };
}
getUserImages(userId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/users/${userId}/images`);
}

getAllUsers(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/users`);
}


getAllImages(): Observable<any> {
  return this.http.get(`${this.baseUrl}`, { headers: this.getAuthHeaders() })
      .pipe(
          map((response: any) => {
              console.log('Risposta dell\'API:', response); 
              return response; 
          }),
          catchError((error) => {
              if (error.status === 401) {
                  console.error('Errore di autenticazione', error);
              }
              return throwError(error);
          })
      );
}

getImageById(id: number): Observable<Blob> {
  return this.http.get(`${this.baseUrl}/${id}/data`, {
    headers: this.getAuthHeaders(),
    responseType: 'blob' 
  });
}
  uploadImage(userId: string, formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/upload`, formData, { 
      headers: this.getAuthHeaders() 
    });
  }
  
  deleteUser(userId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/users/${userId}`);
  }

  deleteImage(imageId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${imageId}`);
  }
}




import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ImageService } from '../services/image.service';

@Component({
  selector: 'app-image-detail',
  templateUrl: './image-detail.component.html',
  styleUrls: ['./image-detail.component.css']
})
export class ImageDetailComponent implements OnInit {
  
  image: any;

  constructor(
    private route: ActivatedRoute,
    private imageService: ImageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const imageId = this.route.snapshot.paramMap.get('id');
  
    if (imageId !== null && !isNaN(+imageId)) {
      const numericImageId = Number(imageId);
  
      this.imageService.getImageById(numericImageId).subscribe(
        (data: Blob) => {
          const reader = new FileReader();
          reader.onload = () => {
            this.image = {
              
              url: reader.result as string
            };
          };
          reader.readAsDataURL(data);
        },
        error => {
          console.error('Errore nel recupero dell\'immagine', error);
        }
      );
    } else {
      console.error('ID immagine non valido');
    }
  }

  
  goBackToGallery(): void {
    this.router.navigate(['/dashboard']); 
  }

}  
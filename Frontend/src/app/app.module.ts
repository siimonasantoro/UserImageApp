import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { HomeComponent } from './home/home.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ImageDetailComponent } from './image-detail/image-detail.component';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AuthGuard } from './auth.guard';
import { AuthInterceptor } from './HTTPInterceptor/auth.interceptor';
import { NgxImageZoomModule } from 'ngx-image-zoom';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { AdminService } from './services/admin.service';
import { AuthenticationService } from './services/authentication-service.service';
import { ImageService } from './services/image.service';
import { JwtModule } from '@auth0/angular-jwt';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component'; 
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';

export function tokenGetter() {
  return localStorage.getItem('authToken');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    HomeComponent,
    DashboardComponent,
    ImageDetailComponent,
    ForgotPasswordComponent,
    AdminDashboardComponent,
    ConfirmDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    NgxImageZoomModule,
    MatDialogModule,
    MatButtonModule,
    MatToolbarModule,
    BrowserAnimationsModule,
    JwtModule.forRoot({ 
      config: {
        tokenGetter: tokenGetter,
        allowedDomains: ['localhost:8080'], 
        disallowedRoutes: ['localhost:8080/login'] 
      }
    }),
  ],
  providers: [
    AuthGuard,
    AdminService,
    AuthenticationService,
    ImageService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

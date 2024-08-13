import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AccueilComponent } from './components/accueil/accueil.component';
import {RouterOutlet} from "@angular/router";
import { AppRoutingModule } from './app-routing.module';
import { MenuComponent } from './components/menu/menu.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import { FooterComponent } from './components/footer/footer.component';
import {LoginComponent} from "./components/auth/login/login.component";
import {RegisterComponent} from "./components/auth/register/register.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NewEvenementComponent } from './components/new-evenement/new-evenement.component';
import { MesParisComponent } from './components/mes-paris/mes-paris.component';
import { NewPariComponent } from './components/new-pari/new-pari.component';
import { AdminComponent } from './components/admin/admin.component';
import { ResultatComponent } from './components/resultat/resultat.component';
import { GestionCompteComponent } from './components/gestion-compte/gestion-compte.component';
import { MesNotificationsComponent } from './components/mes-notifications/mes-notifications.component';
import { ParierComponent } from './components/parier/parier.component';
import {JwtInterceptor} from "./service/jwt.interceptor";
import { ModalConsulterNotificationComponent } from './components/modal-consulter-notification/modal-consulter-notification.component';
import { ModifierEvenementComponent } from './components/modifier-evenement/modifier-evenement.component';

@NgModule({
  declarations: [
    AppComponent,
    AccueilComponent,
    MenuComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    NewEvenementComponent,
    MesParisComponent,
    NewPariComponent,
    AdminComponent,
    ResultatComponent,
    GestionCompteComponent,
    MesNotificationsComponent,
    ParierComponent,
    ModalConsulterNotificationComponent,
    ModifierEvenementComponent
  ],
  imports: [
    BrowserModule,
    RouterOutlet,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  entryComponents: [ModalConsulterNotificationComponent],
  providers: [
    {
      provide: Storage,
      useValue: window.localStorage
    },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

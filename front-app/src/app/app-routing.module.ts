import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {AccueilComponent} from "./components/accueil/accueil.component";
import {LoginComponent} from "./components/auth/login/login.component";
import {RegisterComponent} from "./components/auth/register/register.component";
import {NewEvenementComponent} from "./components/new-evenement/new-evenement.component";
import {AuthGuardServiceService} from "./service/auth-guard-service.service";
import {RoleGuardServiceService} from "./service/role-guard-service.service";
import {MesParisComponent} from "./components/mes-paris/mes-paris.component";
import {NewPariComponent} from "./components/new-pari/new-pari.component";
import {AdminComponent} from "./components/admin/admin.component";
import {ResultatComponent} from "./components/resultat/resultat.component";
import {GestionCompteComponent} from "./components/gestion-compte/gestion-compte.component";
import {MesNotificationsComponent} from "./components/mes-notifications/mes-notifications.component";
import {ParierComponent} from "./components/parier/parier.component";
import {ModifierEvenementComponent} from "./components/modifier-evenement/modifier-evenement.component";

const routes: Routes = [
  {path:"",component:AccueilComponent},
  {path:"connexion",component:LoginComponent},
  {path:"inscription",component:RegisterComponent},
  {path:"mes-paris",component:MesParisComponent,canActivate:[AuthGuardServiceService]},
  {path:"parier/:id",component:NewPariComponent,canActivate: [AuthGuardServiceService]},
  {path:"modifier-evenement/:id",component:ModifierEvenementComponent,canActivate: [AuthGuardServiceService]},
  {path:"mon-compte", component:GestionCompteComponent, canActivate: [AuthGuardServiceService]},
  {path:"mes-notifications",component:MesNotificationsComponent, canActivate: [AuthGuardServiceService]},
  {path:"parier/:id",component:ParierComponent, canActivate: [AuthGuardServiceService]},
  {path:"admin",
    component:AdminComponent,
    canActivate: [RoleGuardServiceService],
    data: {
      expectedRole: 'ROLE_ADMIN'
    }},
  {path:"ajout-evenement",
    component:NewEvenementComponent,
    canActivate: [RoleGuardServiceService],
    data: {
      expectedRole: 'ROLE_ADMIN'
    }},

  {path:"resultat/:id",
    component:ResultatComponent,
    canActivate: [RoleGuardServiceService],
    data: {
      expectedRole: 'ROLE_ADMIN'
    }},
  {path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

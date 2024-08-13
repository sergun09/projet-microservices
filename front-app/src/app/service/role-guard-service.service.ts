import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from "@angular/router";
import {AuthentService} from "./authent.service";

@Injectable({
  providedIn: 'root'
})
export class RoleGuardServiceService implements CanActivate {
  constructor(public authentService: AuthentService, public router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean {
    const expectedRole = route.data["expectedRole"];
    if (!this.authentService.estConnecte()) {
        this.router.navigateByUrl('/connexion');
        return false;
    }
    else if(this.authentService.estConnecte() && !this.authentService.getRoles()?.includes(expectedRole)){
      this.router.navigateByUrl('/');
      return false;
    }
    return true;
  }
}

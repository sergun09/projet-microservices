import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {AuthentService} from "./authent.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardServiceService implements CanActivate {
  constructor(private authentService: AuthentService, private router: Router) {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean{
    if (this.authentService.estConnecte()) {
      return true;
    }

    this.router.navigateByUrl('/connexion');
    return false;
  }
}

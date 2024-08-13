import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthentService} from "../../service/authent.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit{
  estConnecte: any;
  nomPrenomUtilisateur: any;
  estAdmin: any;

  constructor(private authentService:AuthentService,private router: Router) { }

  ngOnInit(): void {
    this.estConnecte = this.authentService.estConnecte();

    if (this.estConnecte) {
      this.estAdmin = this.authentService.estAdmin();
      const nom = this.authentService.getDonneesUtilisateur()?.nom;
      const prenom = this.authentService.getDonneesUtilisateur()?.prenom;
      this.nomPrenomUtilisateur = `${nom} ${prenom}`
    }
    this.authentService.etatAuth.subscribe(state => {
      this.estConnecte = state;
      if (this.estConnecte) {
        this.estAdmin = this.authentService.estAdmin();
        const nom = this.authentService.getDonneesUtilisateur()?.nom;
        const prenom = this.authentService.getDonneesUtilisateur()?.prenom;
        this.nomPrenomUtilisateur = `${nom} ${prenom}`
      }
    });
  }

  handleLogout() {
    this.authentService.deconnexion();
    this.router.navigateByUrl('/');
  }

}

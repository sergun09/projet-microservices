import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Evenement, EvenementDTO} from "../../model/evenement";
import {Parier} from "../../model/parier";
import {Pari} from "../../model/pari";
import {Identifiants} from "../../model/identifiants";
import {EvenementService} from "../../service/evenement.service";
import {PariService} from "../../service/pari.service";
import {PaiementService} from "../../service/paiement.service";
import {Reglement} from "../../model/reglement";
import {AuthentService} from "../../service/authent.service";
import {Compte} from "../../model/compte";

@Component({
  selector: 'app-new-pari',
  templateUrl: './new-pari.component.html',
  styleUrls: ['./new-pari.component.scss']
})
export class NewPariComponent implements OnInit{

  public evenement: EvenementDTO
  private idEvenement: number;
  register: Boolean;
  public prediction: any;
  public errorMessage: string = "";
  public error = false;
  public isPariCreated:boolean=false;

  constructor(private route:ActivatedRoute,private router: Router,
              private authentService:AuthentService,
              private evenementService:EvenementService,
              private pariService:PariService,
              private paiementService:PaiementService) {
  }


  ngOnInit(): void {
    this.getEvenementByID();
  }

  public getEvenementByID(){
    const idParam = this.route.snapshot.paramMap.get('id')
    if(idParam !== null){
      this.idEvenement = parseInt(idParam)
      this.evenementService.getEvenementById(this.idEvenement)
        .subscribe(resultat => {
            this.evenement = resultat;
          },
          error => {
              console.log(error);
            this.router.navigateByUrl('/');
          })
    }
  }

 async onSavePari(mise: string) {
      this.register = true;
    const montant = parseInt(mise);
    const solde = await this.verifierSolde();
    if(solde >= montant){
        let nouvPari: Parier = {
            evenementId : this.evenement.idEvenement,
            transactionId:0,
            prediction: this.prediction,
            mise : montant
        }
        const idPari = await this.enregistrerPari(nouvPari);
        await this.payerPari(idPari,mise);
        if(!this.error){
            this.register = false;
            this.isPariCreated = true;
        }
    }
    else{
        this.register = false;
        this.error = true;
        this.errorMessage = "Solde insuffisant";
    }
  }
  enregistrerPari(nouvPari:Parier):Promise<number>{
      return new Promise( (resolve, reject) =>
        this.pariService.creerPari(nouvPari).subscribe(
      (data:any) => {
          resolve(data.body['id']);
      },
      error => {
          this.errorMessage = error.error.errors.join(" ");
          this.error = true;
          this.register = false;
      }))
  }

  verifierSolde():Promise<number> {
      return new Promise( (resolve, reject)=>
      {
          var solde:number = -1;
          this.paiementService.getCompteByIdUtilisateur(this.authentService.getDonneesUtilisateur()?.idUtilisateur)
              .subscribe(
              (resultat)=>{
                  solde = resultat["solde"];
                  resolve(solde);
              },
              error => {
                  console.log(error);
              }
          )});
  }

  payerPari(idPari:number,mise: string):Promise<void> {
      return new Promise ( (resolve, reject) => {
      const reglement:Reglement = {
      idPari:idPari,
      montant:parseInt(mise)
    }
     this.paiementService.payerPari(reglement).subscribe(
        () => {
            this.error = false;
            resolve();
        },
        error => {
          this.error = true;
          this.errorMessage = error.error.errors.join(" ")
          this.register = false;
        }
    )});
  }

}

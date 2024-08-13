import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {PaiementService} from "../../service/paiement.service";
import {Versement} from "../../model/versement";
import {AuthentService} from "../../service/authent.service";
import {Compte} from "../../model/compte";

@Component({
  selector: 'app-gestion-compte',
  templateUrl: './gestion-compte.component.html',
  styleUrls: ['./gestion-compte.component.scss']
})
export class GestionCompteComponent {
  public compte:Compte;
  public error = false;
  public submitted = false;
  public listeTypeTransaction = [
      'CB => compte parieur',
      'Compte parieur => CB'
  ];
  public isVersement:boolean;
  public selectedTypeTransaction = this.listeTypeTransaction.at(0);
  form:FormGroup = new FormGroup({
    numCarte: new FormControl<number>(1234,[Validators.required]),
    dateExpiration:new FormControl('',[Validators.required]),
    cvv:new FormControl<number>(1234,[Validators.required,Validators.minLength(3)]),
    montant: new FormControl<number>(0.0,[Validators.required]),
    typeTransaction: new FormControl(''),
    idCompte: new FormControl<number>(-1),
  });

  constructor(private router: Router, private paiementService:PaiementService,private authentService:AuthentService) { }
  ngOnInit(): void {
    this.isVersement = false;
    const idUtilisateur = this.authentService.getDonneesUtilisateur()?.idUtilisateur;
    this.paiementService.getCompteByIdUtilisateur(idUtilisateur).subscribe(
        (resultat)=>{
          this.compte = resultat;
        },
        error => {
          console.log(error);
        }
    );
  }
  get idCompte(){
    return this.form.get('idCompte')
  }
  get typeTransaction(){
    return this.form.get('typeTransaction')
  }
  get numCarte(){
    return this.form.get('numCarte')
  }

  get dateExpiration(){
    return this.form.get('dateExpiration')
  }
  get cvv(){
    return this.form.get('cvv')
  }

  get montant(){
    return this.form.get('montant')
  }

  onFilterByTypeTransaction(value:string) {
    this.selectedTypeTransaction = value;
  }

  handleSubmit() {
    this.submitted = true;
    if (this.form.invalid) { return; }
    if(this.selectedTypeTransaction === this.listeTypeTransaction.at(0)){
      this.form.controls['typeTransaction'].setValue('CB_COMPTE_PARIEUR')
    }

    if(this.selectedTypeTransaction === this.listeTypeTransaction.at(1)){
      this.form.controls["typeTransaction"].setValue("COMPTE_PARIEUR_CB");
    }
    this.form.controls['idCompte'].setValue(this.compte.idCompte);
    this.paiementService.versement(<Versement> this.form.value).subscribe(
        () => {
          this.isVersement = true;
    },
    error => {
      this.router.navigateByUrl('/');
      console.log(error);
    })
  }

  rechergerPage(){
    //location.reload();
    this.ngOnInit()
  }

}

import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {EvenementService} from "../../service/evenement.service";
import {EvenementDTO} from "../../model/evenement";

@Component({
  selector: 'app-modifier-evenement',
  templateUrl: './modifier-evenement.component.html',
  styleUrls: ['./modifier-evenement.component.scss']
})
export class ModifierEvenementComponent {
  public submitted = false;
  register: Boolean;
  listeCategories: Array<String>;
  public errorMessage: string = "";
  private idEv: number;
  public evenement: EvenementDTO

  formEvenement = new FormGroup({
    idEvenement:new FormControl<number>(0),
    equipe1:new FormControl('',[Validators.required,Validators.minLength(3)]),
    equipe2:new FormControl('',[Validators.required,Validators.minLength(3)]),
    dateEvenement:new FormControl('',[Validators.required]),//une date antérieur à aujourd'hui
    nomTypeEvenement:new FormControl('',[Validators.required]),
    ville:new FormControl('',[Validators.required,Validators.minLength(3)]),
    coteEquipe1:new FormControl<number>(1,[Validators.required]),
    coteEquipe2:new FormControl<number>(1,[Validators.required]),
    coteNul:new FormControl<number>(1,[Validators.required]),
    typeResultat:new FormControl('')
  });

  constructor(private route:ActivatedRoute,private router: Router,private evenementService:EvenementService) {
    this.register = false;
    this.listeCategories = ["Foot","Basket","Rugby"]
  }

  ngOnInit(): void {
    this.getEvenementByID()
  }

  public getEvenementByID(){
    const idParam = this.route.snapshot.paramMap.get('id')
    if(idParam !== null){
      this.idEv = parseInt(idParam)
      this.evenementService.getEvenementById(this.idEv)
          .subscribe(resultat => {
                this.evenement = resultat;
                this.formEvenement.controls["idEvenement"].setValue(this.evenement.idEvenement);
                this.formEvenement.controls["equipe1"].setValue(this.evenement.equipe1);
                this.formEvenement.controls["equipe2"].setValue(this.evenement.equipe2);
                this.formEvenement.controls["coteEquipe1"].setValue(this.evenement.coteEquipe1);
                this.formEvenement.controls["coteEquipe2"].setValue(this.evenement.coteEquipe2);
                this.formEvenement.controls["coteNul"].setValue(this.evenement.coteNul);
                this.formEvenement.controls["dateEvenement"].setValue(this.evenement.dateEvenement);
                this.formEvenement.controls["nomTypeEvenement"].setValue(this.evenement.nomTypeEvenement);
                this.formEvenement.controls["ville"].setValue(this.evenement.ville);
                this.formEvenement.controls["idEvenement"].setValue(this.evenement.idEvenement);
              },
              error => {
                this.router.navigateByUrl('/');
                console.log(error);
              })
    }
  }

  get typeResultat(){
    return this.formEvenement.get('typeResultat');
  }

  get idEvenement(){
    return this.formEvenement.get('idEvenement');
  }

  get equipe1(){
    return this.formEvenement.get('equipe1');
  }
  get equipe2(){
    return this.formEvenement.get('equipe2');
  }
  get dateEvenement(){
    return this.formEvenement.get('dateEvenement');
  }
  get nomTypeEvenement(){
    return this.formEvenement.get('nomTypeEvenement');
  }
  get ville(){
    return this.formEvenement.get('ville');
  }
  get coteEquipe1(){
    return this.formEvenement.get('coteEquipe1');
  }
  get coteEquipe2(){
    return this.formEvenement.get('coteEquipe2');
  }
  get coteNul(){
    return this.formEvenement.get('coteNul');
  }

  onModifEvenement() {
    this.submitted = true;
    if (this.formEvenement.invalid) {
      return;
    }
    console.log(this.formEvenement.value);
    this.register = true;
    this.evenementService.modifierEvenement(this.idEv,<EvenementDTO> this.formEvenement.value)
        .subscribe((r)=>{
          this.router.navigate(["/admin"])
        }, (error)=>{console.log(error)})
  }

  onSupprEvenement() {
    this.register = true;
    this.evenementService.supprimerEvenement(this.idEv)
        .subscribe((r)=>{
          this.router.navigate(["/admin"])
        }, (error)=>{console.log(error)})
  }
}

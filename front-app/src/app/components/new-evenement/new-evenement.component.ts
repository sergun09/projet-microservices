import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {Evenement, EvenementDTO} from "../../model/evenement";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {EvenementService} from "../../service/evenement.service";
@Component({
  selector: 'app-new-evenement',
  templateUrl: './new-evenement.component.html',
  styleUrls: ['./new-evenement.component.scss']
})
export class NewEvenementComponent {
  public submitted = false;
  register: Boolean;
  listeCategories: Array<String>;
  public errorMessage: string = "";
  public isEvenementsCreated:boolean=false;


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

  constructor(private router: Router, private evenementService:EvenementService) {
    this.register = false;
    this.listeCategories = ["Foot","Basket","Rugby"]
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

  onSaveEvenement() {
    this.submitted = true;
    this.register = true;
    if (this.formEvenement.invalid) {
      this.register = false;
      this.submitted= false;
      return;
    }
    this.evenementService.ajouterEvenement(<EvenementDTO> this.formEvenement.value)
      .subscribe((r)=>{
        this.register = false;
        this.isEvenementsCreated = true;
      }, (error)=>{console.log(error)})
  }
}

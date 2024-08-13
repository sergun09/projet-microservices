import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {Utilisateur} from "../../../model/Utilisateur";
import {AuthentService} from "../../../service/authent.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})

export class RegisterComponent implements OnInit {
  public submitted = false;
  public errorMessages: string[] = [];

  form = new FormGroup({
    email: new FormControl('',[Validators.required,Validators.email]),
    nom:new FormControl('',[Validators.required,Validators.minLength(3)]),
    prenom:new FormControl('',[Validators.required,Validators.minLength(3)]),
    mdpUtilisateur: new FormControl('',[Validators.required,Validators.minLength(4)]),
    plainPassword: new FormControl('',[]),
  });

  constructor(private router: Router,private authentService:AuthentService) { }

  ngOnInit(): void {
    if(this.authentService.estConnecte()){
      this.router.navigateByUrl("");
    }
  }

  get email() {
    return this.form.get('email');
  }

  get nom() {
    return this.form.get('nom');
  }
  get prenom() {
    return this.form.get('prenom');
  }
  get mdpUtilisateur() {
    return this.form.get('mdpUtilisateur');
  }

  get plainPassword(){
    return this.form.get('plainPassword');
  }

  handleSubmit() {
    this.submitted = true;
    if (this.form.invalid) {
      return;
    }

    if(this.form.controls["mdpUtilisateur"].value !== this.form.controls["plainPassword"].value ){
      this.form.get("mdpUtilisateur")?.setErrors({violation: "Mots de passe non identiques !"})
      return;
    }
    const form = this.form.value;
    delete form.plainPassword;
    this.authentService.inscription(<Utilisateur> form).subscribe(
      () => {
        this.router.navigateByUrl('/');
      },
      error => {
        this.errorMessages = []
        if (error.status === 409 || error.status === 400 ) {
          const formProp = this.form.get("email");
          if (formProp) {
            formProp.setErrors({
              violation: error.error.errors.join("\n")
            });
          }
          return;
        }
        else {
          error.error.errors.forEach( (e:string) => this.errorMessages.push(e));
        }
      }
    );
  }

}




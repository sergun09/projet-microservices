import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Identifiants} from "../../../model/identifiants";
import {AuthentService} from "../../../service/authent.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public errorMessages: string[] = [];
  public submitted = false;

  form = new FormGroup({
    email: new FormControl('', [Validators.required,Validators.email]),
    mdpUtilisateur: new FormControl('', Validators.required)
  });

  constructor(private authentService:AuthentService, private router: Router) {
  }

  ngOnInit(): void {
    if(this.authentService.estConnecte()){
      this.router.navigateByUrl("");
    }
  }

  handleSubmit() {
   this.submitted = true;
    if (this.form.invalid) { return; }
    this.authentService.connexion(<Identifiants>this.form.value).subscribe(
      resultat => {
        this.errorMessages = [];
        this.router.navigateByUrl('/');
      },

      error => {
          this.errorMessages = []
          if(error.status === 403){
              this.errorMessages.push("email ou mot de passe incorrect")
          }
          else{
              error.error.errors.forEach( (e:string) => this.errorMessages.push(e))
          }
      }
    );
  }

}
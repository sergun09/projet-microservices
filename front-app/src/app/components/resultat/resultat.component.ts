import {Component, OnInit} from '@angular/core';
import {Evenement, EvenementDTO} from "../../model/evenement";
import {ActivatedRoute, Router} from "@angular/router";
import {Parier} from "../../model/parier";
import {EvenementService} from "../../service/evenement.service";

@Component({
  selector: 'app-resultat',
  templateUrl: './resultat.component.html',
  styleUrls: ['./resultat.component.scss']
})
export class ResultatComponent implements OnInit{

  public evenement: EvenementDTO
  public typeEvenement:string
  private idEvenement: number;
  register: Boolean;
  public resultat: any;
  public errorMessage: string = "";

  constructor(private route:ActivatedRoute,private router: Router,
              public evenementService:EvenementService)
  {
  }


  ngOnInit(): void {
    this.getEvenementByID()
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
                this.router.navigateByUrl('/');
                console.log(error);
              })
    }
  }

  onSaveResultat() {
    this.register = true;
    this.evenementService.saisieResultat(this.idEvenement,this.resultat).subscribe(
      () => {
        this.router.navigateByUrl('/admin');
      },
      error => {
        this.register = false;
        if (error.status === 406){
          this.errorMessage = 'Le résultat que vous avez saisi est impossible !';
          return;
        }
        this.errorMessage = 'Un problème est survenu, veuillez ré-essayer plus tard';
      }
    );
  }
}

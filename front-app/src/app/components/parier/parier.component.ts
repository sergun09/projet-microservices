import { Component } from '@angular/core';
import {Evenement, EvenementDTO} from "../../model/evenement";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-parier',
  templateUrl: './parier.component.html',
  styleUrls: ['./parier.component.scss']
})
export class ParierComponent {
  public evenement: EvenementDTO
  private idEvenement: number;
  register: Boolean;
  public resultat: any;
  public errorMessage: string = "";

  constructor(private route:ActivatedRoute,private router: Router) {
  }


  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id')
    this.getEvenementByID(idParam);
  }

  public getEvenementByID(idParam:any){
    /*if(idParam !== null){
      this.idEvenement = parseInt(idParam)
      this.webService.getMatchById(this.idEvenement)
        .subscribe(data => {
            this.match = data;
          },
          error => {
            this.router.navigateByUrl('/');
            console.log(error);
          })
    }*/
  }

  onSavePari() {
  }
}

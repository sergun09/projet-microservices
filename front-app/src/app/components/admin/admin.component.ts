import { Component } from '@angular/core';
import {Evenement} from "../../model/evenement";
import {Router} from "@angular/router";
import {map} from "rxjs";
import {PageDto} from "../../model/page-dto";
import {HttpClient} from "@angular/common/http";
import {EvenementService} from "../../service/evenement.service";
import {PariService} from "../../service/pari.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent {
  public selectedTypeEvenement:any = null;
  public evenements:PageDto<Evenement>;
  public listTypesEvenement:Set<string>;
  public currentPage : number =0;
  public pageSize : number =5;
  public selectedEvenement:PageDto<Evenement>;

  //Map key = idPari, value= nombre de paris pour l'evenement,
  public nbParisByEvenement:Map<number,number>
  constructor(private http:HttpClient,
              private evenementService:EvenementService,
              private pariService:PariService) {
    this.nbParisByEvenement = new Map<number, number>();
    this.listTypesEvenement = new Set<string>();
  }


  async ngOnInit() {
    await this.getNbParisByEvenement();
    this.rechargerEvenements();
  }

  public rechargerEvenements(){
    this.evenementService.getEvenement(this.currentPage,this.pageSize)
        .subscribe(resultat => {
              console.log(resultat);
              this.evenements = resultat;
              this.selectedEvenement = this.evenements;
              resultat.items.forEach(e => this.listTypesEvenement.add(e.typeEvenement.nomTypeEvenement));
            },
            error => {
              console.log(error);
            })
  }

  getNbParisByEvenement():Promise<void>{
    return new Promise((resolve, reject) => {
      this.pariService.getNbParisByEvenement()
          .subscribe(
              data => {
                data.forEach( (elem:any) =>
                    this.nbParisByEvenement.set(elem["idEvenement"],elem['nbParis'])
                )
                resolve();
              },
              error => {
                console.log(error);
              })
    })
  }

  gotoPage(page: number) {
    this.currentPage=page;
    this.selectedTypeEvenement = null;
    this.rechargerEvenements();
  }

  onFilterByTypeEvenement(value:string) {
    this.selectedTypeEvenement = value;
    const filteredEvenements = this.evenements.items.filter( e =>
        e.typeEvenement.nomTypeEvenement === this.selectedTypeEvenement
    );
    this.selectedEvenement = {
      items: filteredEvenements,
      totalPages:this.evenements.totalPages,
      currentPage:this.evenements.currentPage,
      totalItems:filteredEvenements.length
    }
    console.log(this.selectedEvenement);
  }


  async resetFilter() {
    this.selectedEvenement = this.evenements;
    this.selectedTypeEvenement= null;
  }

}

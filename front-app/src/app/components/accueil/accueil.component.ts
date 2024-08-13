import {Component, NgZone} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, Observer, Subscription} from "rxjs";
import {Pari, PariDTO} from "../../model/pari";
import {Evenement} from "../../model/evenement";
import {PageDto} from "../../model/page-dto";
import {EvenementService} from "../../service/evenement.service";
import {PariService} from "../../service/pari.service";
import {AuthentService} from "../../service/authent.service";

@Component({
  selector: 'app-accueil',
  templateUrl: './accueil.component.html',
  styleUrls: ['./accueil.component.scss']
})
export class AccueilComponent {

  public selectedTypeEvenement:any = null;
  public evenements:PageDto<Evenement>;
  public selectedEvenement:PageDto<Evenement>;
  public listTypesEvenement:Set<string>;
  public currentPage : number =0;
  public pageSize : number =6;
  //Map key = idPari, value= nombre de paris pour l'evenement,
  public nbParisByEvenement:Map<number,number>
  public ready:boolean = false;
  public estConnecte:boolean = false;

  constructor(private http:HttpClient,
              private authentService:AuthentService,
              private evenementService:EvenementService,
              private pariService:PariService) {
    this.nbParisByEvenement = new Map<number, number>();
    //["foot", "basket", "rugby","volley"]
    this.listTypesEvenement = new Set<string>();
  }

  async ngOnInit() {
      this.estConnecte = this.authentService.estConnecte();
    await  this.rechargerEvenements();
    if (this.authentService.estConnecte()){
        await this.getNbParisByEvenement()
    }
    this.ready = true;

  }

  public rechargerEvenements():Promise<void>{
    return new Promise ( (resolve, reject) => this.evenementService.getEvenement(this.currentPage,this.pageSize)
      .subscribe(resultat => {
          this.evenements = resultat;
          this.selectedEvenement = this.evenements;
          resultat.items.forEach(e => this.listTypesEvenement.add(e.typeEvenement.nomTypeEvenement));
          resolve();
          },
        error => {
          console.log(error);
        }))
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
  }

  async resetFilter() {
    this.selectedEvenement = this.evenements;
    this.selectedTypeEvenement= null;
  }

}

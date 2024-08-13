import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Pari, PariDTO} from "../../model/pari";
import {PageDto} from "../../model/page-dto";
import {PariService} from "../../service/pari.service";
import {EvenementDTO} from "../../model/evenement";
import {EvenementService} from "../../service/evenement.service";
@Component({
  selector: 'app-mes-paris',
  templateUrl: './mes-paris.component.html',
  styleUrls: ['./mes-paris.component.scss']
})
export class MesParisComponent implements OnInit {

    public selectedTypeEvenement:any = null;
    public paris:PageDto<Pari>|any;
    public parisDTOPage:PageDto<PariDTO>;
    public selectedParisDTOPage:PageDto<PariDTO>;
    public listTypesEvenement:Set<any>;
    public errorMessage: string = "";
    public ready: boolean = false;
    public currentPage : number =1;
    public pageSize : number =6;

  constructor(private router: Router,public pariService:PariService,
              public evenementService:EvenementService) {
    this.listTypesEvenement = new Set<any>();
  }


  ngOnInit(): void {
    this.rechargerMesParis();
  }

  public rechargerMesParis(){
    this.getMesParis();
  }

  public async getMesParis(){
      await this.getMesParisFromPariService();
      await this.parisToParisDTO();
     this.ready = true;
  }

    public getMesParisFromPariService():Promise<void>{
         return new Promise((resolve, reject) =>  this.pariService.getMesParis(this.currentPage,this.pageSize)
            .subscribe(data => {
                    this.paris = data;
                    resolve();
                },
                error => {
                    this.router.navigateByUrl('/');
                    console.log(error);
                }))
    }

    public parisToParisDTO():Promise<void>{
      const parisDTO:Array<PariDTO> = new Array();
      return new Promise((resolve, reject) => {
          this.paris.items.forEach((p: any) => {
              this.evenementService.getEvenementById(p.evenementId)
                  .toPromise().then(
                  data => {
                      const pariDTO: PariDTO = {
                          id: p.id,
                          evenement: data,
                          transactionId: p.transactionId,
                          mise: p.mise,
                          prediction: p.prediction

                      }
                      this.listTypesEvenement.add(data?.nomTypeEvenement)
                      parisDTO.push(pariDTO);
                  },
                  error => {
                      this.router.navigateByUrl('/');
                      console.log(error);
                  })
          })
          this.parisDTOPage = {
              currentPage: this.paris.currentPage,
              totalPages: this.paris.totalPages,
              totalItems: this.paris.totalItems,
              items: parisDTO
          }
          this.selectedParisDTOPage = this.parisDTOPage;
          resolve();
      })
    }

    gotoPage(page: number) {
        this.currentPage=page;
        this.selectedTypeEvenement = null;
        this.rechargerMesParis();
    }

  onRemovePari(idPari: number) {
    this.pariService.annulerPari(idPari).subscribe(
      () => {
        this.rechargerMesParis();
      },
      error => {
          console.log(error);
      }
    );
  }

    onFilterByTypeEvenement(value:any) {
        this.selectedTypeEvenement = value;
        const filteredParis = this.parisDTOPage.items.filter( p =>
            p.evenement?.nomTypeEvenement === this.selectedTypeEvenement
        );
        this.selectedParisDTOPage = {
            items: filteredParis,
            totalPages:this.parisDTOPage.totalPages,
            currentPage:this.parisDTOPage.currentPage,
            totalItems:filteredParis.length
        }
        console.log(this.selectedParisDTOPage);
    }

    async resetFilter() {
        this.selectedParisDTOPage = this.parisDTOPage;
        this.selectedTypeEvenement= null;
    }
}


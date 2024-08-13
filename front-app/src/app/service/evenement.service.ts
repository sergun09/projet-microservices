import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment.development";
import {map, Observable, Observer} from "rxjs";
import {Evenement, EvenementDTO} from "../model/evenement";
import {PageDto} from "../model/page-dto";

const BASE_URL = `${environment.SERVICE_EVENEMENT}`;
@Injectable({
  providedIn: 'root'
})
export class EvenementService {

      constructor(private http: HttpClient) { }

      ajouterEvenement(nouvEvenement: EvenementDTO) :Observable<boolean>{
        return this.http.post(`${BASE_URL}`,
            nouvEvenement,{observe: 'response'})
            .pipe(map((response)=>response.status===201))
      }

      public getEvenement(page:number,size:number): Observable<PageDto<Evenement>>{
        return this.http.get<any>(`${BASE_URL}?page=${page}&size=${size}`)
            .pipe(map((resultat) => {
              return resultat
            }));
      }

      public getEvenementById(id:number): Observable<EvenementDTO>{
        return this.http.get<any>(`${BASE_URL}/${id}`)
            .pipe(map((resultat) => {
              return resultat
            }));
      }

      saisieResultat(idEvenement: number, resultat: any) {
          const data = {"typeResultat":resultat}
            return this.http.patch(`${BASE_URL}/${idEvenement}/resultat`,
                data,{observe:'response'})
                .pipe(map(response=> {return response}))
      }

    modifierEvenement(idEvenement: number, evenement: EvenementDTO) {
        return this.http.patch(`${BASE_URL}/${idEvenement}`,
            evenement,{observe:'response'})
            .pipe(map(response=> {return response}))
    }

    supprimerEvenement(idEvenement: number) {
        return this.http.delete(`${BASE_URL}/${idEvenement}`,{observe:'response'})
            .pipe(map(response=> {return response}))
    }


}

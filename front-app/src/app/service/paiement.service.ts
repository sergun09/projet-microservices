import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.development";
import {Identifiants} from "../model/identifiants";
import {map, Observable} from "rxjs";
import {Versement} from "../model/versement";
import {Reglement} from "../model/reglement";
import {Compte} from "../model/compte";

const BASE_URL = `${environment.SERVICE_PAIEMENT}`;
@Injectable({
  providedIn: 'root'
})
export class PaiementService {

  constructor(private http: HttpClient) { }

    public getCompteByIdUtilisateur(idUtilisateur:any): Observable<Compte>{
        return this.http.get<Compte>(`${BASE_URL}/comptes/utilisateurs/${idUtilisateur}`)
            .pipe(map((resultat) => {
                return resultat
            }));
    }

  versement(versement: Versement) {
    return this.http.post<any>(`${BASE_URL}/versements`,versement, {observe: 'response'})
        .pipe(map((resultat) => {
          return resultat
        }));
  }

  payerPari(reglement:Reglement) {
    return this.http.post<any>(`${BASE_URL}/reglements`,reglement, {observe: 'response'})
        .pipe(map((resultat) => {
          return resultat
        }));
  }

}

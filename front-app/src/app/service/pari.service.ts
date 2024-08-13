import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment.development";
import {map, Observable, Observer} from "rxjs";
import {Pari} from "../model/pari";
import {Parier} from "../model/parier";
import {PageDto} from "../model/page-dto";

const BASE_URL = `${environment.SERVICE_PARI}`;
@Injectable({
  providedIn: 'root'
})
export class PariService {

    constructor(private http: HttpClient) { }

    creerPari(nouvPari: Parier){
        return this.http.post(`${BASE_URL}`,
            nouvPari,{observe:'response'})
            .pipe(map(response=> {return response}))
    }

    public getMesParis(page:number,size:number): Observable<PageDto<Pari>>{
        return this.http.get<any>(`${BASE_URL}/utilisateurs?pageIndex=${page}&pageSize=${size}`)
            .pipe(map((resultat) => {
              return resultat
            }));
    }

    getNbParisByEvenement(){
        return this.http.get<any>(`${BASE_URL}/evenements`)
            .pipe(map((resultat) => {
                return resultat
            }));
    }

    annulerPari(idPari: number){
        return this.http.delete(`${BASE_URL}/${idPari}`)
            .pipe(map(response=> {return response}))
    }

}

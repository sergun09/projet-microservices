import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment.development";
import {map, Observable} from "rxjs";
import {Evenement} from "../model/evenement";
import {Compte} from "../model/compte";
import {PageDto} from "../model/page-dto";
import {NotificationUtilisateur} from "../model/notification-utilisateur";

const BASE_URL = `${environment.SERVICE_NOTIFICATIONS}`;
@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http: HttpClient) { }

  public getNotificationsByIdUtilisateur(idUtilisateur:any, page:number,size:number): Observable<PageDto<NotificationUtilisateur>>{
    return this.http.get<PageDto<NotificationUtilisateur>>(`${BASE_URL}/utilisateur/${idUtilisateur}?page=${page}&size=${size}`)
        .pipe(map((resultat) => {
          return resultat
        }));
  }
  
}

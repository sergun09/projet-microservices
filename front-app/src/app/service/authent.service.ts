import {Injectable, NgZone} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {Identifiants} from "../model/identifiants";
import {environment} from "../../environments/environment.development";
import {map, Subject} from "rxjs";
import {jwtDecode, JwtPayload} from "jwt-decode";
import {Utilisateur} from "../model/Utilisateur";

const BASE_URL = `${environment.SERVICE_AUTH}`;

interface CustomJwtPayload extends JwtPayload {
    idUtilisateur: string;
    nom: string;
    prenom: string;
    roles:string[];
}
@Injectable({
  providedIn: 'root'
})
export class AuthentService {
    etatAuth = new Subject<boolean>();

    constructor(private http: HttpClient, private storage: Storage) { }

    connexion(identifiants: Identifiants) {
        return this.http.post<any>(`${BASE_URL}/login`,identifiants,{observe:'response'})
            .pipe(
                map(( resultat) => {
                    const authorizationValues = resultat.headers.getAll("Authorization");
                    if(authorizationValues){
                        const token = authorizationValues[0].split(" ")[1];
                        this.storage.setItem("token",token);
                    }
                    this.etatAuth.next(true);
                    return resultat
                })
            );
    }

    inscription(identifiants:Identifiants) {
        return this.http.post<any>(`${BASE_URL}/inscriptions`,identifiants, {observe:"response"})
            .pipe(
                map(( resultat) => {
                    const authorizationValues = resultat.headers.getAll("Authorization");
                    if(authorizationValues){
                        const token = authorizationValues[0].split(" ")[1];
                        this.storage.setItem("token",token);
                    }
                    this.etatAuth.next(true);
                    return resultat;
                })
            );
    }

    deconnexion() {
        this.storage.removeItem('token');
        this.etatAuth.next(false);
    }


    estConnecte() {
        if(this.getTokenUtilisateur()!==null && !this.etatAuth.observed){
            this.etatAuth.next(true);
        }
        return this.getTokenUtilisateur()!==null;
    }

    getDonneesUtilisateur() {
        const jwtToken:string|null = this.getTokenUtilisateur();
        if(jwtToken!== null){
            try {
                return jwtDecode<CustomJwtPayload>(jwtToken);
            } catch(Error) {
                return null;
            }
        }
        return null;
    }

    getTokenUtilisateur() {
       return this.storage.getItem("token") !==null ? this.storage.getItem("token") : null;
    }

    estAdmin() {
        if(this.estConnecte()){
            return this.getDonneesUtilisateur()?.roles.includes("ROLE_ADMIN") == true;
        }
        return false;
    }

    getRoles():Array<string>| undefined{
        return  this.getDonneesUtilisateur()?.roles;
    }
}

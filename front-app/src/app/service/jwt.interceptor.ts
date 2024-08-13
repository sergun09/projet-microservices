import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthentService} from "./authent.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

    constructor(private authentService:AuthentService) {}

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        const token = this.authentService.getTokenUtilisateur();
        if(token && this.authentService.estConnecte()){
            request = request.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });
        }
        return next.handle(request);
    }




}
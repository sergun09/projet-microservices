import { Component } from '@angular/core';
import {Route, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'pel';

  constructor(private router:Router) {

  }

  routeIsActive(route: string) {
    return this.router.url == route;
  }
}

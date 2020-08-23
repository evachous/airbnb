import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {DataService} from "../services/data.service";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ChatGuard implements CanActivate {
  accommodationID: number;
  guestUsername: string;

  constructor(
    private router: Router,
    private dataService: DataService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const username = localStorage.getItem('username');
    if (username) {
      this.accommodationID = parseInt(route.paramMap.get('id'));
      this.guestUsername = route.paramMap.get('guestUsername');

      return this.dataService.chatCheck(this.accommodationID, this.guestUsername, username).pipe(map(
        res => {
          if (!res) {
            this.router.navigate(['/'], { queryParams: { returnUrl: state.url }});
          }
          return res;
        }
      ));
    }
  }

}

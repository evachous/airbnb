import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DataService } from '../services/data.service';

@Injectable({
  providedIn: 'root'
})
export class GuestGuard implements CanActivate {
  constructor(
    private router: Router,
    private dataService: DataService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const username = localStorage.getItem('username');
    if (username) {
      return this.dataService.guestCheck(username).pipe(map(
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

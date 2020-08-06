import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DataService } from '../services/data.service';

@Injectable({
  providedIn: 'root'
})
export class HostGuard implements CanActivate {
  constructor(
    private router: Router,
    private dataService: DataService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const username = localStorage.getItem('username');
    if (username) {
      return this.dataService.hostCheck(username).pipe(map(
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

import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class SignupGuard implements CanActivate {
  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (localStorage.getItem('token')) {
      // logged in so redirect to home page with the return url
      this.router.navigate(['/'], { queryParams: { returnUrl: state.url }});
      return false;
    }
    // not logged in so return true
    return true;
  }
}

import { Injectable } from '@angular/core';
import {AuthenticationService} from './authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor(
    private authenticationService: AuthenticationService
  ) { }

  changeMessage(message): void {
    localStorage.removeItem('message');
    this.authenticationService.setToken('message', message);
  }

  get getMessage(): string {
    return localStorage.getItem('message');
  }
}

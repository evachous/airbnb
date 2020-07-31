import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import { Login } from '../model/login';
import { User } from '../model/user';
import { map } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public loggedin = false;
  public currentUser = new Subject();
  public username: string;

  constructor(private http: HttpClient) {
    const localUser = this.getTokenUsername;
    if (localUser == null || localUser === 'undefined') {
      // console.log('NO TOKEN');
      this.currentUser.next(null);
      this.loggedin = false;
    }
    else {
      // console.log('sending ' + localStorage.getItem('username'));
      this.currentUser.next(localUser);
      this.loggedin = true;
    }
  }

  login(username: string, password: string): Observable<HttpResponse<string>> {
    const ln: Login = { username, password };
    return this.http.post<string>('https://localhost:8443/login', ln, { observe: 'response'});
  }

  setToken(tokenKey, tokenValue): void {
    // console.log('storing token ' + tokenKey);
    localStorage.setItem(tokenKey, tokenValue);
    if (tokenKey === 'username') {
      this.currentUser.next(tokenValue);
      this.loggedin = true;
    }
  }

  changeCurrentUsername(newUsername): void {
    localStorage.removeItem('username');
    this.setToken('username', newUsername);
  }

  get getTokenUsername(): string {
    return localStorage.getItem('username');
  }

  logout(): void {
    // remove token from local storage to log user out
    this.currentUser.next(null);
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('message');
    this.loggedin = false;
  }
}

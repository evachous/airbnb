import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', Accept: 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private url = 'https://localhost:8443/';

  constructor( private http: HttpClient ) { }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.url + 'users');
  }
  getUser(id): Observable<User> {
    const tempurl = this.url + 'users/' + id;
    return this.http.get<User>(tempurl);
  }
}

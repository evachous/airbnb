import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { Observable } from 'rxjs';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {Form, FormGroup} from '@angular/forms';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', Accept: 'application/json', 'Access-Control-Allow-Origin': '*' })
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
    return this.http.get<User>(this.url + 'users/' + id);
  }

  signup(formData: FormData): Observable<HttpResponse<string>> {
    console.log('sending request....');
    return this.http.post<string>(this.url + 'signup', formData, {observe: 'response'});
  }

  changeInfo(formData: FormData): Observable<HttpResponse<string>> {
    console.log('sending update....');
    return this.http.post<string>(this.url + 'settings', formData, {observe: 'response'});
  }
}

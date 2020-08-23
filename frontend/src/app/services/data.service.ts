import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { Observable } from 'rxjs';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Accommodation} from "../model/accommodation";
import {Chat} from "../model/chat";

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

  getUser(username): Observable<User> {
    return this.http.get<User>(this.url + 'users/' + username);
  }

  getUserPicture(username): any {
    return this.http.get(this.url + 'getUserPicture/' + username, {responseType: "text"});
  }

  signup(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'signup', formData, {observe: 'response'});
  }

  changeInfo(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'changeInfo', formData, {observe: 'response'});
  }

  changePassword(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'changePassword', formData, {observe: 'response'});
  }

  adminCheck(username): Observable<boolean> {
    return this.http.get<boolean>(this.url + 'adminCheck/' + username);
  }

  hostCheck(username): Observable<boolean> {
    return this.http.get<boolean>(this.url + 'hostCheck/' + username);
  }

  approveHost(username): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'approveHost', username, {observe: 'response'});
  }

  addAccommodation(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'addAccommodation', formData, {observe: 'response'});
  }

  getAccommodation(id): Observable<Accommodation> {
    return this.http.get<Accommodation>(this.url + 'accommodations/' + id);
  }

  getAccommodationImage(id, image): any {
    return this.http.get(this.url + 'getAccommodationImage/' + id + '/' + image,{responseType: "text"});
  }

  createChat(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'createChat', formData, {observe: 'response'});
  }

  getChat(accommodationID, guestUsername): Observable<Chat> {
    return this.http.get<Chat>(this.url + 'getChat/' + accommodationID + '/' + guestUsername);
  }

  sendMessage(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'sendMessage', formData, {observe: 'response'});
  }
}


import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { Observable } from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {Accommodation} from "../model/accommodation";
import {Chat} from "../model/chat";
import {Reservation, Review} from "../model/reservation";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json', Accept: 'application/json',
    'Access-Control-Allow-Origin': '*' })
};

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private url = 'https://localhost:8443/';

  constructor( private http: HttpClient ) { }

  // users

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

  guestCheck(username): Observable<boolean> {
    return this.http.get<boolean>(this.url + 'guestCheck/' + username);
  }

  approveHost(username): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'approveHost', username, {observe: 'response'});
  }

  addressLookup(lat, lng): any {
    const nomUrl = 'https://nominatim.openstreetmap.org/reverse?format=json&lat=' + lat + '&lon=' + lng +
      '&addressdetails=1';
    return this.http.get(nomUrl);
  }

  // accommodation

  addAccommodation(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'addAccommodation', formData, {observe: 'response'});
  }

  getAllAccommodations(): Observable<Accommodation[]> {
    return this.http.get<Accommodation[]>(this.url + 'allAccommodations/');
  }

  getAccommodation(id): Observable<Accommodation> {
    return this.http.get<Accommodation>(this.url + 'accommodations/' + id);
  }

  getAccommodationImage(id, index): any {
    return this.http.get(this.url + 'getAccommodationImage/' + id + '/' + index,{responseType: "text"});
  }

  searchAccommodations(params: HttpParams): Observable<Accommodation[]> {
    return this.http.get<Accommodation[]>(this.url + 'searchAccommodations', {params: params});
  }

  getRecommendations(username): Observable<Accommodation[]> {
    return this.http.get<Accommodation[]>(this.url + 'getRecommendations/' + username);
  }

  changeAccommodation(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'changeAccommodation', formData, {observe: 'response'});
  }

  deleteAccommodationImage(id, index): Observable<{}> {
    return this.http.delete(this.url + 'deleteAccommodationImage/' + id + '/' + index, httpOptions);
  }

  // chat

  createChat(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'createChat', formData, {observe: 'response'});
  }

  getChat(accommodationID, guestUsername, currentUsername): Observable<Chat> {
    return this.http.get<Chat>(this.url + 'getChat/' + accommodationID + '/' + guestUsername + '/' + currentUsername);
  }

  getGuestChats(username): Observable<Chat[]> {
    return this.http.get<Chat[]>(this.url + 'getGuestChats/' + username);
  }

  getAccommodationChats(id): Observable<Chat[]> {
    return this.http.get<Chat[]>(this.url + 'getAccommodationChats/' + id);
  }

  chatCheck(accommodationID, guestUsername, currentUsername): Observable<boolean> {
    return this.http.get<boolean>(this.url + 'chatCheck/' + accommodationID + '/' + guestUsername + '/' + currentUsername);
  }

  sendMessage(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'sendMessage', formData, {observe: 'response'});
  }

  deleteChat(chatID): Observable<{}> {
    return this.http.delete(this.url + 'deleteChat/' + chatID, httpOptions);
  }

  // reservation

  makeReservation(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'makeReservation', formData, {observe: 'response'});
  }

  checkDateAvailability(id, checkin, checkout): Observable<boolean> {
    return this.http.get<boolean>(this.url + 'checkDateAvailability/' + id + '/' + checkin + '/' + checkout);
  }

  getAllReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.url + 'allReservations/');
  }

  getAccommodationReservations(id): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.url + 'getAccommodationReservations/' + id);
  }

  getGuestReservations(username): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.url + 'getGuestReservations/' + username);
  }

  // review

  addReview(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'addReview', formData, {observe: 'response'});
  }

  getAccommodationReviews(id): Observable<Review[]> {
    return this.http.get<Review[]>(this.url + 'getAccommodationReviews/' + id);
  }

  getGuestReviews(username): Observable<Review[]> {
    return this.http.get<Review[]>(this.url + 'getGuestReviews/' + username);
  }

  getHostReviews(username): Observable<Review[]> {
    return this.http.get<Review[]>(this.url + 'getHostReviews/' + username);
  }

  getAllReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.url + 'allReviews/');
  }

  // search history
  addSearchAccommodation(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'addSearchAccommodation', formData, {observe: 'response'});
  }

  addSearchAddress(formData: FormData): Observable<HttpResponse<string>> {
    return this.http.post<string>(this.url + 'addSearchAddress', formData, {observe: 'response'});
  }
}


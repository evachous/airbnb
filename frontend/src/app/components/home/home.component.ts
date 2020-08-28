import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import {Router} from "@angular/router";
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NgbDate, NgbCalendar} from '@ng-bootstrap/ng-bootstrap';
import {OpenStreetMapProvider} from 'leaflet-geosearch';
import {AuthenticationService} from "../../services/authentication.service";
import {User} from "../../model/user";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  username: string;
  user: User;

  searchForm: FormGroup;
  hoveredDate: NgbDate | null = null;
  minDate: NgbDate;

  provider: any;
  results: any;
  input: any;
  value: '';

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private calendar: NgbCalendar,
    private authenticationService: AuthenticationService,
  ) {
    this.minDate = calendar.getToday();
  }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    if (this.username != null) {
      this.dataService.getUser(this.username).subscribe(user => {
        this.user = user;
      }, error => {
        this.user = null;
      })
    }
    else
      this.user = null;

    this.provider = new OpenStreetMapProvider();

    this.searchForm = this.formBuilder.group({
      location: ['', Validators.required],
      lat: ['', Validators.required],
      lng: ['', Validators.required],
      checkin: [null, Validators.required],
      checkout: [null, Validators.required],
      guests: ['', Validators.required]
    });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.searchForm.controls;
  }

  isHovered(date: NgbDate): boolean {
    return this.f.checkin.value && !this.f.checkout.value && this.hoveredDate && date.after(this.f.checkin.value)
      && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate): boolean {
    return this.f.checkout.value && date.after(this.f.checkin.value) && date.before(this.f.checkout.value);
  }

  isRange(date: NgbDate): boolean {
    return date.equals(this.f.checkin.value) || (this.f.checkout.value && date.equals(this.f.checkout.value))
      || this.isInside(date) || this.isHovered(date);
  }

  onChange(e): void {
    this.provider.search({ query: e.target.value }).then((result: any) => {
      this.results = result;
    });
  }

  onResultClick(r): void {
    this.value = r.label;
    this.f.location.setValue(r.label);
    this.f.lat.setValue(r.y);
    this.f.lng.setValue(r.x);
    this.results = [];
  }

  onSubmit(): void {
    this.router.navigate(['/search'],{ queryParams: {
        location: this.f.location.value,
        lat: this.f.lat.value,
        lng: this.f.lng.value,
        checkin: this.f.checkin.value.year + '-' + this.f.checkin.value.month + '-'
          + this.f.checkin.value.day,
        checkout: this.f.checkout.value.year + '-' + this.f.checkout.value.month + '-'
          + this.f.checkout.value.day,
        guests: this.f.guests.value
      }})
  }
}

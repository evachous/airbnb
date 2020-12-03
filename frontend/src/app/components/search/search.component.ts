import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {AbstractControl, FormBuilder, FormGroup} from "@angular/forms";
import {Accommodation, Address} from "../../model/accommodation";
import {HttpParams} from "@angular/common/http";
import {DataService} from "../../services/data.service";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  queryParams: Params;
  filterParams: Params[] = new Array<Params>();
  missing = false;

  filterForm: FormGroup;

  accommodations: Accommodation[];
  emptyAcc: boolean;
  accommodationsImages: string[] = new Array<string>();

  recommendations: Accommodation[];
  emptyRec: boolean;
  recommendationsImages: string[] = new Array<string>();

  currentUsername: string = null;

  page = 1;
  pageSize = 10;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private authenticationService: AuthenticationService,
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.queryParams = params;

      if (this.queryParams.location == undefined || this.queryParams.lat == undefined ||
        this.queryParams.lng == undefined || this.queryParams.checkin == undefined ||
        this.queryParams.checkout == undefined || this.queryParams.guests == undefined) {
        this.missing = true;
      }
      else {
        this.currentUsername = this.authenticationService.getTokenUsername;
        this.initFilters();
        this.getRecommendations();
        this.getResults();
        //this.addSearchAddress();
      }

    })
  }

  addSearchAddress(): void {
    const formData = new FormData();
    formData.append('username', this.currentUsername);
    formData.append('lat', this.queryParams.lat);
    formData.append('lng', this.queryParams.lng);

    this.dataService.addSearchAddress(formData).subscribe(
      response => {},
      error => { console.log(error) }
      );
  }

  initFilters(): void {
    this.filterForm = this.formBuilder.group({
      type: this.queryParams.type || null,
      maxCost: this.queryParams.maxCost || null,
      internet: this.queryParams.internet || null,
      ac: this.queryParams.ac || null,
      heating: this.queryParams.heating || null,
      kitchen: this.queryParams.kitchen || null,
      tv: this.queryParams.tv || null,
      parking: this.queryParams.parking || null,
      elevator: this.queryParams.elevator || null
    });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.filterForm.controls;
  }

  changeType(newType: string) {
    this.f.type.setValue(newType);
  }

  onSubmit(): void {
    this.router.navigate(['/search'],{ queryParams: {
        type: this.f.type.value || null,
        maxCost: this.f.maxCost.value || null,
        internet: this.f.internet.value || null,
        ac: this.f.ac.value || null,
        heating: this.f.heating.value || null,
        kitchen: this.f.kitchen.value || null,
        tv: this.f.tv.value || null,
        parking: this.f.parking.value || null,
        elevator: this.f.elevator.value || null,
      }, queryParamsHandling: 'merge'})

    this.getResults();
  }

  getRecommendations(): void {
    this.dataService.getRecommendations(this.currentUsername).subscribe(rec => {
      this.recommendations = rec;
      this.emptyRec = this.recommendations.length == 0;

      for (let i = 0; i < this.recommendations.length; i++) {
        if (!this.recommendations[i].images.length)
          this.recommendationsImages[i] = 'http://placehold.it/150x150';
        else {
          this.dataService.getAccommodationImage(this.recommendations[i].id, 0).subscribe(image => {
            this.recommendationsImages[i] = 'data:image/jpeg;base64,' + image;
          })
        }
      }
    })
  }

  getResults(): void {
    let params = new HttpParams().set('lat', this.queryParams.lat).set('lng', this.queryParams.lng).set('checkin', this.queryParams.checkin)
      .set('checkout', this.queryParams.checkout).set('guests', this.queryParams.guests).set('type', this.f.type.value)
      .set('maxCost', this.f.maxCost.value).set('internet', this.f.internet.value).set('ac', this.f.ac.value)
      .set('heating', this.f.heating.value).set('kitchen', this.f.kitchen.value).set('tv', this.f.tv.value)
      .set('parking', this.f.parking.value).set('elevator', this.f.elevator.value);

    this.dataService.searchAccommodations(params).subscribe(acc => {
      this.accommodations = acc;
      this.emptyAcc = this.accommodations.length == 0;

      this.accommodations.sort(compareCosts);

      for (let i = 0; i < this.accommodations.length; i++) {
        if (!this.accommodations[i].images.length)
          this.accommodationsImages[i] = 'http://placehold.it/150x150';
        else {
          this.dataService.getAccommodationImage(this.accommodations[i].id, 0).subscribe(image => {
            this.accommodationsImages[i] = 'data:image/jpeg;base64,' + image;
          })
        }
      }
    },error => {
      console.log(error);
    })
  }
}

function compareCosts(a1: Accommodation, a2: Accommodation): number {
  if (a1.info.minCost < a2.info.minCost)
    return -1;
  if (a1.info.minCost > a2.info.minCost)
    return 1;
  return 0;
}

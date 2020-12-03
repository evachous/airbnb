import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {User} from "../../model/user";
import {DataService} from "../../services/data.service";
import {Accommodation, Address} from "../../model/accommodation";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {AlertService} from "../../services/alert.service";
import {NgbDate, NgbCalendar} from '@ng-bootstrap/ng-bootstrap';
import {latLng, MapOptions, tileLayer, Map, Marker, icon} from 'leaflet';
import {OpenStreetMapProvider} from 'leaflet-geosearch';

@Component({
  selector: 'app-accommodations',
  templateUrl: './hostaccommodations.component.html',
  styleUrls: ['./hostaccommodations.component.css']
})
export class HostaccommodationsComponent implements OnInit {
  username: string;
  user: User;
  accommodations: Accommodation[] = null;
  accommodationsImages: string[] = new Array<string>();
  emptyAcc: boolean;

  map: Map;
  mapOptions: MapOptions;
  addressResults: any;
  addressInput: any;
  addressValue: '';
  marker: Marker = null;
  provider: OpenStreetMapProvider;
  selectedAddress: Address = new Address();

  selectedImages: FileList;

  infoForm: FormGroup;
  locationForm: FormGroup;
  rulesForm: FormGroup;

  message: string = null;
  successMessage: boolean;

  hoveredDate: NgbDate | null = null;
  minDate: NgbDate;
  invalidDate: boolean;

  page = 1;
  pageSize = 10;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private dataService: DataService,
    private alertService: AlertService,
    private calendar: NgbCalendar
  ) {
    this.minDate = calendar.getToday();
  }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.dataService.getUser(this.username).subscribe( user => {
      this.user = user;
      this.accommodations = this.user.accommodations;
      this.emptyAcc = this.accommodations.length == 0;

      for (let i = 0; i < this.accommodations.length; i++) {
        if (!this.accommodations[i].images.length)
          this.accommodationsImages[i] = 'http://placehold.it/150x150'
        else {
          this.dataService.getAccommodationImage(this.accommodations[i].id, 0).subscribe(image => {
            this.accommodationsImages[i] = 'data:image/jpeg;base64,' + image;
          })
        }
      }

      this.message = this.alertService.getMessage;
      this.successMessage = this.message != null && (this.message === 'Added accommodation successfully!');
      localStorage.removeItem('message');

      this.initForms();
      this.initMapOptions();
    });
  }

  get f1(): { [p: string]: AbstractControl } {
    return this.infoForm.controls;
  }

  get f2(): { [p: string]: AbstractControl } {
    return this.locationForm.controls;
  }

  get f3(): { [p: string]: AbstractControl } {
    return this.rulesForm.controls;
  }

  initForms(): void {
    this.infoForm = this.formBuilder.group({
      name: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      minCost: ['', Validators.required],
      costPerPerson: ['', Validators.required],
      type: ['', Validators.required],
      beds: ['', Validators.required],
      bedrooms: ['', Validators.required],
      bathrooms: ['', Validators.required],
      area: ['', Validators.required],
      description: ['', Validators.required],
      images: ['', Validators.required],
      livingRoom: false,
      internet: false,
      ac: false,
      heating: false,
      kitchen: false,
      tv: false,
      parking: false,
      elevator: false
    });

    this.locationForm = this.formBuilder.group({
      address: [null, Validators.required],
      transportation: ['', Validators.required]
    });

    this.rulesForm = this.formBuilder.group({
      smoking: false,
      pets: false,
      events: false,
      minDays: ['', Validators.required],
      maxDays: ['', Validators.required],
      maxPeople: ['', Validators.required]
    });
  }

  initMapOptions(): void {
    this.mapOptions = {
      center: latLng(51.505, 0),
      zoom: 9,
      layers: [
        tileLayer(
          'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          {
            maxZoom: 18,
            attribution: 'Map data © OpenStreetMap contributors'
          })
      ],
    };
  }

  onMapReady(map: Map) {
    this.map = map;

    this.provider = new OpenStreetMapProvider({
      params: {
        addressdetails: 1, // include additional address detail parts
      },
    });
  }

  onAddressChange(e): void {
    this.provider.search({ query: e.target.value }).then((result: any) => {
      this.addressResults = result;
    }, error => {
      console.log(error);
    });
  }

  // normal geocoding
  onResultClick(r): void {
    console.log(r);
    this.addressValue = r.label;
    this.fillAddress(r.raw.address, r.label, r.y, r.x);
    this.addressResults = [];

    if (this.marker != null) {
      this.map.removeLayer(this.marker);
    }
    this.marker = new Marker([r.y, r.x])
      .setIcon(
        icon({
          iconSize: [25, 41],
          iconAnchor: [13, 41],
          popupAnchor: [1, -34],
          iconUrl: 'assets/marker-icon.png'
        }));
    this.marker.addTo(this.map);

    this.marker.bindPopup(r.label).openPopup();

    this.map.fitBounds(r.bounds);
  }

  // reverse geocoding
  onMapClick(e) {
    this.dataService.addressLookup(e.latlng.lat, e.latlng.lng)
      .subscribe(r => {
        this.addressValue = r.display_name;
        this.fillAddress(r.address, r.display_name, r.lat, r.lon);

        if (this.marker != null) {
          this.map.removeLayer(this.marker);
        }
        this.marker = new Marker(e.latlng)
          .setIcon(
            icon({
              iconSize: [25, 41],
              iconAnchor: [13, 41],
              popupAnchor: [1, -34],
              iconUrl: 'assets/marker-icon.png'
            }));
        this.marker.addTo(this.map);

        this.marker.bindPopup(this.addressValue).openPopup();

        this.map.fitBounds([[parseFloat(r.boundingbox[0]), parseFloat(r.boundingbox[2])],
          [parseFloat(r.boundingbox[1]), parseFloat(r.boundingbox[3])]]);
      }, error => {
        console.log(error);
      })
  }

  fillAddress(address, label, lat, lng): void {
    this.selectedAddress.label = label;

    if (address.house_number != null)
      this.selectedAddress.number = address.house_number;
    else
      this.selectedAddress.number = null;

    this.selectedAddress.road = address.road;
    this.selectedAddress.suburb = address.suburb;

    if (address.city == null)
      this.selectedAddress.city = address.municipality;
    else
      this.selectedAddress.city = address.city;

    this.selectedAddress.state = address.state;
    this.selectedAddress.postcode = address.postcode;
    this.selectedAddress.country = address.country;

    this.selectedAddress.lat = lat;
    this.selectedAddress.lng = lng;

    this.f2.address.setValue(this.selectedAddress);
  }

  isHovered(date: NgbDate): boolean {
    return this.f1.startDate.value && !this.f1.endDate.value && this.hoveredDate && date.after(this.f1.startDate.value)
      && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate): boolean {
    return this.f1.endDate.value && date.after(this.f1.startDate.value) && date.before(this.f1.endDate.value);
  }

  isRange(date: NgbDate): boolean {
    return date.equals(this.f1.startDate.value) || (this.f1.endDate.value && date.equals(this.f1.endDate.value))
      || this.isInside(date) || this.isHovered(date);
  }

  changeType(newType: string) {
    this.f1.type.setValue(newType);
  }

  onFileChange(event): void {
    if (event.target.files && event.target.files.length) {
      this.selectedImages = event.target.files;
    }
  }

  onSubmit(): void {
    const jsonInfo = this.stringifyForm(this.infoForm);
    const jsonLocation = this.stringifyForm(this.locationForm);
    const jsonRules = this.stringifyForm(this.rulesForm);

    const formData = new FormData();
    formData.append('info', jsonInfo);
    formData.append('location', jsonLocation);
    formData.append('rules', jsonRules);
    formData.append('username', this.username);
    formData.append('startDate',this.f1.startDate.value.year + '-' + this.f1.startDate.value.month + '-'
                                          + this.f1.startDate.value.day);
    formData.append('endDate', this.f1.endDate.value.year + '-' + this.f1.endDate.value.month + '-'
                                          + this.f1.endDate.value.day);

    for (let i = 0; i < this.selectedImages.length; i++) {
      formData.append('images', this.selectedImages[i]);
    }

    this.dataService.addAccommodation(formData)
      .subscribe(
        response => {
          this.alertService.changeMessage('Added accommodation successfully!');
          window.location.reload();
        },
        (error: HttpErrorResponse) => {
          this.alertService.changeMessage('Error adding accommodation');
          window.location.reload();
        }
      )
  }

  stringifyForm(form: FormGroup): string {
    return JSON.stringify(form.value, ((key, val) => {
      if (key === 'startDate' || key === 'endDate') {
        return null;
      }
      else if (key === 'images') {
        return undefined;
      }
      else {
        return val;
      }
    }));
  }
}

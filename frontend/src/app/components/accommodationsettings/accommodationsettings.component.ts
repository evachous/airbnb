import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Accommodation, Address} from "../../model/accommodation";
import {AuthenticationService} from "../../services/authentication.service";
import {DataService} from "../../services/data.service";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NgbDate, NgbDateParserFormatter, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {icon, latLng, Map, MapOptions, Marker, tileLayer} from "leaflet";
import {GeoSearchControl, OpenStreetMapProvider} from 'leaflet-geosearch';
import {HttpErrorResponse} from "@angular/common/http";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-accommodationsettings',
  templateUrl: './accommodationsettings.component.html',
  styleUrls: ['./accommodationsettings.component.css']
})
export class AccommodationsettingsComponent implements OnInit {
  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  accommodationImages: string[] = new Array<string>();

  infoForm: FormGroup;
  locationForm: FormGroup;
  rulesForm: FormGroup;

  message: string = null;
  successMessage: boolean;

  oldMap: Map;
  oldMapOptions: MapOptions;
  marker: Marker = null;
  map: Map;
  mapOptions: MapOptions;
  provider: OpenStreetMapProvider;
  searchControl: GeoSearchControl;
  selectedAddress: Address = new Address();

  selectedImages: FileList;
  addedImages = false;

  hoveredDate: NgbDate | null = null;
  minDate: NgbDate;
  invalidDate: boolean;
  startDate: NgbDateStruct;
  endDate: NgbDateStruct;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private dataService: DataService,
    private authenticationService: AuthenticationService,
    private alertService: AlertService,
    private parserFormatter: NgbDateParserFormatter
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.accommodationID = params.id;

      this.loadSettings();
    });
  }

  loadSettings(): void {
    this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
      this.accommodation = acc;
      this.found = true;

      for (let i = 0; i < this.accommodation.images.length; i++) {
        this.dataService.getAccommodationImage(this.accommodationID, i).subscribe(image => {
          this.accommodationImages[i] = 'data:image/jpeg;base64,' + image;
        })
      }

      this.message = this.alertService.getMessage;
      this.successMessage = this.message != null && (this.message === 'Changed accommodation settings successfully!');
      localStorage.removeItem('message');

      this.initForms();
      this.initMapOptions();
    },error => {
      this.found = false;
    })
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
    this.startDate = this.parserFormatter.parse(this.accommodation.info.startDate);
    this.endDate = this.parserFormatter.parse(this.accommodation.info.endDate);
    this.infoForm = this.formBuilder.group({
      name: this.accommodation.info.name,
      startDate: this.startDate,
      endDate: this.endDate,
      minCost: this.accommodation.info.minCost,
      costPerPerson: this.accommodation.info.costPerPerson,
      type: this.accommodation.info.type,
      beds: this.accommodation.info.beds,
      bedrooms: this.accommodation.info.bedrooms,
      bathrooms: this.accommodation.info.bathrooms,
      area: this.accommodation.info.area,
      description: this.accommodation.info.description,
      images: [''],
      livingRoom: this.accommodation.info.livingRoom,
      internet: this.accommodation.info.internet,
      ac: this.accommodation.info.ac,
      heating: this.accommodation.info.heating,
      kitchen: this.accommodation.info.kitchen,
      tv: this.accommodation.info.tv,
      parking: this.accommodation.info.parking,
      elevator: this.accommodation.info.elevator
    });

    this.locationForm = this.formBuilder.group({
      address: this.accommodation.location.address,
      transportation: this.accommodation.location.transportation
    });

    this.rulesForm = this.formBuilder.group({
      smoking: this.accommodation.rules.smoking,
      pets: this.accommodation.rules.pets,
      events: this.accommodation.rules.events,
      minDays: this.accommodation.rules.minDays,
      maxPeople: this.accommodation.rules.maxPeople
    });
  }

  initMapOptions(): void {
    this.oldMapOptions = {
      center: latLng(this.accommodation.location.address.lat, this.accommodation.location.address.lng),
      zoom: 20,
      layers: [
        tileLayer(
          'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          {
            maxZoom: 18,
            attribution: 'Map data © OpenStreetMap contributors'
          })
      ],
    };

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

  onOldMapReady(oldMap: Map) {
    this.oldMap = oldMap;

    this.marker = new Marker([this.accommodation.location.address.lat, this.accommodation.location.address.lng])
      .setIcon(
        icon({
          iconSize: [25, 41],
          iconAnchor: [13, 41],
          popupAnchor: [1, -34],
          iconUrl: 'assets/marker-icon.png'
        }));
    this.marker.addTo(this.oldMap);

    this.marker.bindPopup(this.accommodation.location.address.label).openPopup();
  }

  onMapReady(map: Map) {
    this.map = map;

    this.provider = new OpenStreetMapProvider({
      params: {
        addressdetails: 1, // include additional address detail parts
      },
    });

    this.searchControl = new GeoSearchControl({
      provider: this.provider,
      style: 'bar',
      showMarker: true,
      showPopup: true,
      marker: {
        icon: icon({
          iconSize: [25, 41],
          iconAnchor: [13, 41],
          popupAnchor: [0, -34],
          iconUrl: 'assets/marker-icon.png'
        })
      },
      autoClose: true,
      keepResult: true
    }).addTo(map);

    this.map.on('geosearch/showlocation', (result: any) => {
      this.fillAddress(result.location.raw.address, result.location.label, result.location.y, result.location.x);
      this.f2.address.setValue(this.selectedAddress);
    });
  }

  fillAddress(address, label, lat, lng): void {
    this.selectedAddress.label = label;
    this.selectedAddress.road = address.road;

    if (address.city == null) {
      this.selectedAddress.city = address.municipality;
    }
    else {
      this.selectedAddress.city = address.city;
    }

    this.selectedAddress.country = address.country;
    this.selectedAddress.postcode = address.postcode;
    this.selectedAddress.suburb = address.suburb;

    if (address.house_number != null) {
      this.selectedAddress.number = address.house_number;
    }
    else {
      this.selectedAddress.number = null;
    }

    this.selectedAddress.lat = lat;
    this.selectedAddress.lng = lng;
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
      this.addedImages = true;
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
    formData.append('id', this.accommodationID.toString());
    formData.append('startDate',this.f1.startDate.value.year + '-' + this.f1.startDate.value.month + '-'
      + this.f1.startDate.value.day);
    formData.append('endDate', this.f1.endDate.value.year + '-' + this.f1.endDate.value.month + '-'
      + this.f1.endDate.value.day);

    if (this.addedImages) {
      for (let i = 0; i < this.selectedImages.length; i++) {
        formData.append('images', this.selectedImages[i]);
      }
    }

    this.dataService.changeAccommodation(formData)
      .subscribe(
        response => {
          this.alertService.changeMessage('Changed accommodation settings successfully!');
          window.location.reload();
        },
        (error: HttpErrorResponse) => {
          this.alertService.changeMessage('Error changing accommodation settings');
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

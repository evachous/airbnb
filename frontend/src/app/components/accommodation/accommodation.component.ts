import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {DataService} from "../../services/data.service";
import {Accommodation} from "../../model/accommodation";
import {User} from "../../model/user";
import {AuthenticationService} from "../../services/authentication.service";
import {HttpErrorResponse} from "@angular/common/http";
import {icon, LatLng, latLng, Map, MapOptions, Marker, tileLayer} from "leaflet";

@Component({
  selector: 'app-accommodation',
  templateUrl: './accommodation.component.html',
  styleUrls: ['./accommodation.component.css']
})
export class AccommodationComponent implements OnInit {
  queryParams: Params;
  missingParams = false;

  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  accommodationImages: string[] = new Array<string>();
  host: User;
  hostPicture: any;
  currentUsername: string = null;
  currentUser: User = null;

  map: Map;
  mapOptions: MapOptions;
  marker: Marker = null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private dataService: DataService,
    private authenticationService: AuthenticationService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.queryParams = params;
      console.log(this.queryParams);
      if (this.queryParams.location == undefined || this.queryParams.lat == undefined ||
        this.queryParams.lng == undefined || this.queryParams.checkin == undefined ||
        this.queryParams.checkout == undefined || this.queryParams.guests == undefined) {
        this.missingParams = true;
      }
      this.accommodationID = params.id;
      this.currentUsername = this.authenticationService.getTokenUsername;

      if (this.currentUsername != null) {
        this.dataService.getUser(this.currentUsername).subscribe(user => {
          this.currentUser = user;
        }, error => {
          // this.found = false;
          this.currentUser = null;
        })
      }

      this.loadAccommodation();
    });

  }

  loadAccommodation(): void {
    this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
      this.accommodation = acc;
      this.host = this.accommodation.host;
      this.found = true;

      for (let i = 0; i < this.accommodation.images.length; i++) {
        this.dataService.getAccommodationImage(this.accommodationID, i).subscribe(image => {
          this.accommodationImages[i] = 'data:image/jpeg;base64,' + image;
        })
      }

      this.initMapOptions();

      this.dataService.getUserPicture(this.host.username).subscribe(pic => {
        if (pic === '')
          this.hostPicture = 'http://placehold.it/150x150';
        else
          this.hostPicture = 'data:image/jpeg;base64,' + pic;
        }, error => {
          this.hostPicture = 'http://placehold.it/150x150';
          console.log(error);
      });

      },
      error => {
        this.found = false;
      })
  }

  onChatClick(): void {
    const formData = new FormData();
    formData.append('accommodationID', this.accommodationID.toString());
    formData.append('guestUsername', this.currentUsername);

    this.dataService.createChat(formData).subscribe(response => {
      this.router.navigate(['/chat', this.accommodationID, this.currentUsername]);
    },(error: HttpErrorResponse) => {
      console.log(error);
    })
  }

  initMapOptions(): void {
    this.mapOptions = {
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
  }

  onMapReady(map: Map) {
    this.map = map;

    this.marker = new Marker([this.accommodation.location.address.lat, this.accommodation.location.address.lng])
      .setIcon(
        icon({
          iconSize: [25, 41],
          iconAnchor: [13, 41],
          popupAnchor: [1, -34],
          iconUrl: 'assets/marker-icon.png'
        }));
    this.marker.addTo(this.map);

    this.marker.bindPopup(this.accommodation.location.address.label).openPopup();
  }

  onTabChange() {
    this.map.invalidateSize();
  }

}

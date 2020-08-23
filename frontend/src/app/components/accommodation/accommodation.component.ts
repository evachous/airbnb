import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {DataService} from "../../services/data.service";
import {Accommodation} from "../../model/accommodation";
import {User} from "../../model/user";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-accommodation',
  templateUrl: './accommodation.component.html',
  styleUrls: ['./accommodation.component.css']
})
export class AccommodationComponent implements OnInit {
  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  accommodationImages: string[] = new Array<string>();
  host: User;
  currentUsername: string;
  hostPicture: any;

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private authenticationService: AuthenticationService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.accommodationID = params.id;
      this.currentUsername = this.authenticationService.getTokenUsername;
    });

    this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
      this.accommodation = acc;
      this.host = this.accommodation.host;
      this.found = true;

      for (let i = 0; i < this.accommodation.images.length; i++) {
        this.dataService.getAccommodationImage(this.accommodationID, i).subscribe(image => {
          this.accommodationImages[i] = 'data:image/jpeg;base64,' + image;
          console.log(this.accommodationImages[i]);
        })
      }

      this.dataService.getUserPicture(this.host.username).subscribe(pic => {
        if (pic === '')
          this.hostPicture = 'http://placehold.it/150x150';
        else
          this.hostPicture = 'data:image/jpeg;base64,' + pic;
        },
        error => {
          this.hostPicture = 'http://placehold.it/150x150';
          console.log(error);
      });

      },
      error => {
        this.found = false;
      })
  }

}

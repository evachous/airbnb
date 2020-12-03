import { Component, OnInit } from '@angular/core';
import {Accommodation} from "../../model/accommodation";
import {User} from "../../model/user";
import {Reservation} from "../../model/reservation";
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../../services/data.service";
import {AuthenticationService} from "../../services/authentication.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-accommodationreservations',
  templateUrl: './accommodationreservations.component.html',
  styleUrls: ['./accommodationreservations.component.css']
})
export class AccommodationreservationsComponent implements OnInit {
  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  reservations: Reservation[] = new Array<Reservation>();
  emptyReservations: boolean;
  guestPictures: string[] = new Array<string>();
  host: User;
  currentUsername: string;

  page = 1;
  pageSize = 10;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private dataService: DataService,
    private authenticationService: AuthenticationService,
    private modalService: NgbModal
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.accommodationID = params.id;
      this.currentUsername = this.authenticationService.getTokenUsername;

      this.loadAccommodationReservations();
    });
  }

  loadAccommodationReservations(): void {
    this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
      this.accommodation = acc;
      this.host = this.accommodation.host;

      if (this.host.username != this.currentUsername) {
        this.found = false;
      } else {
        this.found = true;

        this.dataService.getAccommodationReservations(this.accommodationID)
          .subscribe(reservations => {
            this.reservations = reservations;
            this.emptyReservations = this.reservations.length == 0;

            for (let i = 0; i < this.reservations.length; i++) {
              this.dataService.getUserPicture(this.reservations[i].guest.username)
                .subscribe(pic => {
                  if (pic === '')
                    this.guestPictures[i] = 'http://placehold.it/150x150';
                  else
                    this.guestPictures[i] = 'data:image/jpeg;base64,' + pic;
                }, error => {
                  this.guestPictures[i] = 'http://placehold.it/150x150';
                  console.log(error);
                })
            }
          })
      }
    },error => {
      this.found = false;
    })
  }

}

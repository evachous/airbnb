import { Component, OnInit } from '@angular/core';
import {Accommodation} from "../../model/accommodation";
import {User} from "../../model/user";
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../../services/data.service";
import {AuthenticationService} from "../../services/authentication.service";
import {Chat} from "../../model/chat";

@Component({
  selector: 'app-accommodationchats',
  templateUrl: './accommodationchats.component.html',
  styleUrls: ['./accommodationchats.component.css']
})
export class AccommodationchatsComponent implements OnInit {
  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  chats: Chat[];
  emptyChats: boolean;
  guestPictures: string[] = new Array<string>();
  host: User;
  currentUsername: string;

  page = 1;
  pageSize = 2;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private dataService: DataService,
    private authenticationService: AuthenticationService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.accommodationID = params.id;
      this.currentUsername = this.authenticationService.getTokenUsername;

      this.loadAccommodationChats();
    });
  }

  loadAccommodationChats(): void {
    this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
      this.accommodation = acc;
      this.host = this.accommodation.host;

      if (this.host.username != this.currentUsername) {
        this.found = false;
      }
      else {
        this.found = true;
        this.chats = this.accommodation.chats;
        this.emptyChats = this.chats.length == 0;

        for (let i = 0; i < this.chats.length; i++) {
          this.dataService.getUserPicture(this.chats[i].guest.username)
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
      }

    },error => {
      this.found = false;
    })
  }

}
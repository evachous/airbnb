import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {DataService} from "../../services/data.service";
import {User} from "../../model/user";
import {Accommodation} from "../../model/accommodation";
import {Chat} from "../../model/chat";

@Component({
  selector: 'app-hostchats',
  templateUrl: './hostchats.component.html',
  styleUrls: ['./hostchats.component.css']
})
export class HostchatsComponent implements OnInit {
  username: string;
  user: User;
  accommodations: Accommodation[] = null;
  accommodationsImages: string[] = new Array<string>();
  emptyAcc: boolean;
  chats: number[] = new Array<number>();

  page = 1;
  pageSize = 10;

  constructor(
    private authenticationService: AuthenticationService,
    private dataService: DataService,
  ) { }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.dataService.getUser(this.username).subscribe( user => {
      this.user = user;
      this.accommodations = this.user.accommodations;
      this.emptyAcc = this.accommodations.length == 0;

      for (let i = 0; i < this.accommodations.length; i++) {
        if (!this.accommodations[i].images.length)
          this.accommodationsImages[i] = 'http://placehold.it/150x150';
        else {
          this.dataService.getAccommodationImage(this.accommodations[i].id, 0).subscribe(image => {
            this.accommodationsImages[i] = 'data:image/jpeg;base64,' + image;
          })
        }

        this.dataService.getAccommodationChats(this.accommodations[i].id).subscribe(chats => {
          this.chats[i] = chats.length;
        })
      }
    })
  }

}

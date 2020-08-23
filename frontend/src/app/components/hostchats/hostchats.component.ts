import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {DataService} from "../../services/data.service";
import {User} from "../../model/user";
import {Accommodation} from "../../model/accommodation";

@Component({
  selector: 'app-hostchats',
  templateUrl: './hostchats.component.html',
  styleUrls: ['./hostchats.component.css']
})
export class HostchatsComponent implements OnInit {
  username: string;
  user: User;
  accommodations: Accommodation[] = null;
  accommodationsImages: string[][] = new Array<string[]>();
  emptyAcc: boolean;

  page = 1;
  pageSize = 2;

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
        console.log(this.accommodations[i].id);
        console.log(this.accommodations[i].images.length);

        this.accommodationsImages[i] = new Array<string>();

        for (let j = 0; j < this.accommodations[i].images.length; j++) {
          this.dataService.getAccommodationImage(this.accommodations[i].id, j).subscribe(image => {
            this.accommodationsImages[i][j] = 'data:image/jpeg;base64,' + image;
          })
        }
      }
    })
  }

}

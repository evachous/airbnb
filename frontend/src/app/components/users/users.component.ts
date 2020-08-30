import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import { User } from '../../model/user';
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: User[] = new Array<User>();
  userPictures: string[] = new Array<string>();

  accommodationsJSON: any;
  accommodationsXML: any;
  reservationsJSON: any;
  reservationsXML: any;
  guestReviewsJSON: any;
  guestReviewsXML: any;
  hostReviewsJSON: any;
  hostReviewsXML: any;

  page = 1;
  pageSize = 2;

  constructor(
    private dataService: DataService,
    private sanitizer: DomSanitizer
  ) { }

  ngOnInit(): void {
    this.initDownloads();

    this.dataService.getUsers().subscribe(users => {
      this.users = users;

      for (let i = 0; i < this.users.length; i++) {
        this.dataService.getUserPicture(this.users[i].username)
          .subscribe(pic => {
            if (pic === '')
              this.userPictures[i] = 'http://placehold.it/150x150';
            else
              this.userPictures[i] = 'data:image/jpeg;base64,' + pic;
          }, error => {
            this.userPictures[i] = 'http://placehold.it/150x150';
            console.log(error);
          })
      }

    });
  }

  initDownloads(): void {
    this.dataService.getAllAccommodations().subscribe(acc => {
      let json = JSON.stringify(acc);
      this.accommodationsJSON = this.sanitizer.bypassSecurityTrustUrl(
        "data:text/json;charset=utf-8," + encodeURIComponent(json)
      )
      //this.accommodationsXML = this.xmlBuilder.buildObject(json);
    },error => {
      console.log(error);
    })

    this.dataService.getAllReservations().subscribe(res => {
      let json = JSON.stringify(res[0], ((key, val) => {
        if (key === 'guest' || key === 'accommodation') {
          key.toUpperCase();
          return val.id;
        }
        else if (key === 'review' && val != null) {
          return val.id;
        }
        else {
          return val;
        }
      }));

      json = json.replace("guest", "guest_id");
      json = json.replace("accommodation", "accommodation_id");
      json = json.replace("review", "review_id");

      this.reservationsJSON = this.sanitizer.bypassSecurityTrustUrl(
        "data:text/json;charset=utf-8," + encodeURIComponent(json)
      )
      //this.reservationsXML = create(json);
    },error => {
      console.log(error);
    })
  }
}

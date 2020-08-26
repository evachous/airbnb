import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {DataService} from "../../services/data.service";
import {User} from "../../model/user";
import {Chat} from "../../model/chat";

@Component({
  selector: 'app-guestchats',
  templateUrl: './guestchats.component.html',
  styleUrls: ['./guestchats.component.css']
})
export class GuestchatsComponent implements OnInit {
  username: string;
  user: User;
  chats: Chat[];
  emptyChats: boolean;
  hostPictures: string[] = new Array<string>();

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
      this.chats = this.user.chats;
      this.emptyChats = this.chats.length == 0;

      for (let i = 0; i < this.chats.length; i++) {
        console.log(this.chats[i].guestRead);
        console.log(this.chats[i].hostRead);

        this.dataService.getUserPicture(this.chats[i].accommodation.host.username)
          .subscribe(pic => {
            if (pic === '')
              this.hostPictures[i] = 'http://placehold.it/150x150';
            else
              this.hostPictures[i] = 'data:image/jpeg;base64,' + pic;
          }, error => {
            this.hostPictures[i] = 'http://placehold.it/150x150';
            console.log(error);
          })
      }
    })
  }

}

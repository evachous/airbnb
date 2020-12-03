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
  chats: Chat[] = new Array<Chat>();
  emptyChats: boolean;
  hostPictures: string[] = new Array<string>();

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
      //this.chats = this.user.chats;
      this.dataService.getGuestChats(this.user.username).subscribe(chats => {
        this.chats = chats;
        this.emptyChats = this.chats.length == 0;

        this.chats.sort(compareChats);

        for (let i = 0; i < this.chats.length; i++) {
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
    })
  }

}

function compareChats(c1: Chat, c2: Chat): number {
  let t1;
  let t2;
  if (c1.messages.length)
    t1 = new Date(c1.messages[c1.messages.length - 1].timestamp);
  else t1 = null;

  if (c2.messages.length)
    t2 = new Date(c2.messages[c2.messages.length-1].timestamp);
  else t2 = null;

  if (t1 < t2)
    return 1;
  if (t1 > t2)
    return -1;
  return 0;
}

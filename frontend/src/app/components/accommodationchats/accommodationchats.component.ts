import { Component, OnInit } from '@angular/core';
import {Accommodation} from "../../model/accommodation";
import {User} from "../../model/user";
import {ActivatedRoute, Router} from "@angular/router";
import {DataService} from "../../services/data.service";
import {AuthenticationService} from "../../services/authentication.service";
import {Chat} from "../../model/chat";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ModalComponent} from "../modal/modal.component";

@Component({
  selector: 'app-accommodationchats',
  templateUrl: './accommodationchats.component.html',
  styleUrls: ['./accommodationchats.component.css']
})
export class AccommodationchatsComponent implements OnInit {
  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  chats: Chat[] = new Array<Chat>();
  emptyChats: boolean;
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

      this.loadAccommodationChats();
    });
  }

  loadAccommodationChats(): void {
    this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
      this.accommodation = acc;
      this.host = this.accommodation.host;

      if (this.host.username != this.currentUsername) {//TODO: make separate guard
        this.found = false;
      }
      else {
        this.found = true;
        //this.chats = this.accommodation.chats;
        this.dataService.getAccommodationChats(this.accommodationID).subscribe(chats => {
          this.chats = chats;
          this.emptyChats = this.chats.length == 0;

          this.chats.sort(compareChats);

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
        })

      }

    },error => {
      this.found = false;
    })
  }

  openModal(c: Chat): void {
    this.modalService.open(ModalComponent)
      .result.then((result) => {
      this.onDeleteChat(c);
    }, dismiss => {
    });
  }

  onDeleteChat(c: Chat): void {
    this.dataService.deleteChat(c.id).subscribe(response => {
      this.loadAccommodationChats();
      window.location.reload();
    },error => {
      console.log(error);
    });
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

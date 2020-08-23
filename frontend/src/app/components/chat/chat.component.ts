import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {DataService} from "../../services/data.service";
import {Accommodation} from "../../model/accommodation";
import {User} from "../../model/user";
import {Chat, ChatMessage} from "../../model/chat";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../../services/authentication.service";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  host: User;
  hostPicture: any;
  guestUsername: string;
  currentUsername: string;
  chat: Chat;
  chatMessages: ChatMessage[];
  messageForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.accommodationID = params.id;
      this.guestUsername = params.guestUsername;
      this.currentUsername = this.authenticationService.getTokenUsername;

      this.loadChat();
    })
  }

  loadChat() {
    this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
      this.accommodation = acc;
      this.host = this.accommodation.host;
      this.found = true;

      this.dataService.getUserPicture(this.host.username).subscribe(pic => {
        if (pic === '')
          this.hostPicture = 'http://placehold.it/150x150';
        else
          this.hostPicture = 'data:image/jpeg;base64,' + pic;
      }, error => {
        this.hostPicture = 'http://placehold.it/150x150';
        console.log(error);
      });

      this.dataService.getChat(this.accommodationID, this.guestUsername)
        .subscribe(chat => {
          this.chat = chat;
          this.chatMessages = this.chat.messages;

          this.messageForm = this.formBuilder.group({
            message: ['', Validators.required]
          });
        },error => {
          this.found = false;
        })

    },error => {
      this.found = false;
    })
  }

  onSend(): void {
    console.log(this.messageForm.controls.message.value);

    const formData = new FormData();
    formData.append('chatID', this.chat.id.toString());
    formData.append('senderUsername', this.currentUsername);
    formData.append('message', this.messageForm.controls.message.value);

    this.dataService.sendMessage(formData).subscribe(response => {
      console.log('Sent ' + this.messageForm.controls.message.value);
      this.messageForm.reset();

      this.dataService.getChat(this.accommodationID, this.guestUsername)
        .subscribe(chat => {
          this.chat = chat;
          this.chatMessages = this.chat.messages;
        },error => {
          this.found = false;
        })

    },error => {
      console.log(error);
    })
  }
}

import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {User} from "../../model/user";
import {DataService} from "../../services/data.service";

@Component({
  selector: 'app-accommodations',
  templateUrl: './accommodations.component.html',
  styleUrls: ['./accommodations.component.css']
})
export class AccommodationsComponent implements OnInit {
  username: string;
  user: User;

  constructor(
    private authenticationService: AuthenticationService,
    private dataService: DataService,
  ) { }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.dataService.getUser(this.username).subscribe( user => {
      this.user = user;
    });
  }

}

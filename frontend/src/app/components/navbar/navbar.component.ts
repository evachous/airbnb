import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {User} from '../../model/user';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  loggedin = false;
  username: any;
  user: User;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUser.subscribe(name => {
      this.username = name;
      this.loggedin = !(this.username == null || this.username === 'undefined');
    });
  }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.loggedin = !(this.username == null || this.username === 'undefined');
  }

  logout(): void {
    this.authenticationService.logout();
  }
}

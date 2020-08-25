import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {User} from '../../model/user';
import {Router} from '@angular/router';
import {DataService} from '../../services/data.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  loggedin = false;
  isAdmin = false;
  isHost = false;
  isGuest = false;
  username: any;
  user: User;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private dataService: DataService
  ) {
    this.authenticationService.currentUser.subscribe(name => {
      this.username = name;
      this.loggedin = !(this.username == null || this.username === 'undefined');
      this.findRoles();
    }, error => {
      console.log(error);
    });
  }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.loggedin = !(this.username == null || this.username === 'undefined');
    this.findRoles();
  }

  findRoles(): void {
    if (this.loggedin) {
      this.dataService.getUser(this.username).subscribe( user => {
        this.user = user;
      });
    }
  }

  logout(): void {
    this.authenticationService.logout();
  }
}

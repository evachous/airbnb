import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DataService} from '../../services/data.service';
import {User} from '../../model/user';
import {AlertService} from '../../services/alert.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.css']
})
export class UserprofileComponent implements OnInit {
  found = true;
  username: string;
  user: User;
  img: any;
  message: string = null;
  successMessage: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dataService: DataService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.username = params.username;

      this.dataService.getUserPicture(this.username).subscribe(pic => {
        if (pic === '')
          this.img = 'http://placehold.it/150x150';
        else
          this.img = 'data:image/jpeg;base64,' + pic;
      },
        error => {
          this.img = 'http://placehold.it/150x150';
          console.log(error);
      });

      this.dataService.getUser(this.username).subscribe( user => {
        this.user = user;
        this.found = true;

        this.message = this.alertService.getMessage;
        this.successMessage = this.message != null && (this.message === 'Host is now approved!');
        localStorage.removeItem('message');
      },
        error => {
        this.found = false;
      });
    });
  }

  onApprove(): void {
    this.dataService.approveHost(this.username)
      .subscribe(
        response => {
          this.alertService.changeMessage('Host is now approved!');
          window.location.reload();
        },
        (error: HttpErrorResponse) => {
          this.alertService.changeMessage('Error: couldn\'t approve host');
          window.location.reload();
        }
      );
  }
}

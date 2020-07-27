import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DataService} from '../../services/data.service';
import {User} from '../../model/user';

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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private dataService: DataService
  ) { }

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username');
    this.dataService.getUser(this.username).subscribe( user => {
      this.user = user;
      this.found = true;
      if (this.user.profilePicture === null) {
        this.img = 'http://placehold.it/150x150';
      }
      else {
        this.img = 'data:image/jpeg;base64,' + this.user.profilePicture;
      }
    },
      error => {
        this.found = false;
        // this.router.navigate(['/']);
      });
  }
}

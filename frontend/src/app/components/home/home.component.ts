import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import { AuthenticationService } from '../../services/authentication.service';

import { User } from '../../model/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  users: User[];
  user: User;
  currentUser: string;
  loading = false;
  constructor(private dataService: DataService) { }

  ngOnInit(): void {
    // const id = this.route.snapshot.paramMap.get('id');
    const id = 1;
    this.dataService.getUser(id).subscribe( user => this.user = user);
  }

}

import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  users: User[] = new Array<User>();
  userPictures: string[] = new Array<string>();

  page = 1;
  pageSize = 2;

  constructor(
    private dataService: DataService
  ) { }

  ngOnInit(): void {
    this.dataService.getUsers().subscribe(users => {
      this.users = users;

      for (let i = 0; i < this.users.length; i++) {
        this.dataService.getUserPicture(this.users[i].username)
          .subscribe(pic => {
            if (pic === '')
              this.userPictures[i] = 'http://placehold.it/150x150';
            else
              this.userPictures[i] = 'data:image/jpeg;base64,' + pic;
          }, error => {
            this.userPictures[i] = 'http://placehold.it/150x150';
            console.log(error);
          })
      }

    });
  }


}

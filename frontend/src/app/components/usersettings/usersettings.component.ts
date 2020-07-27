import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {DataService} from '../../services/data.service';
import {User} from '../../model/user';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-usersettings',
  templateUrl: './usersettings.component.html',
  styleUrls: ['./usersettings.component.css']
})
export class UsersettingsComponent implements OnInit {
  username: string;
  user: User;
  img: string;
  userForm: FormGroup;
  newPicture: any;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private dataService: DataService
  ) { }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.dataService.getUser(this.username).subscribe( user => {
      this.user = user;
      if (this.user.profilePicture === null) {
        this.img = 'http://placehold.it/150x150';
      }
      else {
        this.img = 'data:image/jpeg;base64,' + this.user.profilePicture;
      }

      this.initForm();
    });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.userForm.controls;
  }

  initForm(): void {
    this.userForm = this.formBuilder.group({
      firstName: [this.user.firstName],
      lastName: [this.user.lastName],
      username: [this.user.username],
      email: [this.user.email, [Validators.email]],
      password: [this.user.password, [Validators.required, Validators.minLength(4)]],
      confirmPassword: [this.user.password, [Validators.required, Validators.minLength(4)]],
      phone: [this.user.phone, [Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]],
      isAdmin: this.user.isAdmin,
      isHost: this.user.isHost,
      isGuest: this.user.isGuest,
    }, { validator: [confirmPasswordValidator] });
  }

  onFileChange(event): void {
    if (event.target.files && event.target.files.length) {
      this.newPicture = event.target.files[0];
    }
  }

}

function confirmPasswordValidator(c: AbstractControl): { mismatch: boolean } {
  if (c.get('password').value !== c.get('confirmPassword').value) {
    return {mismatch: true};
  }
}

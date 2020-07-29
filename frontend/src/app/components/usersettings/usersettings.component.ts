import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {DataService} from '../../services/data.service';
import {User} from '../../model/user';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-usersettings',
  templateUrl: './usersettings.component.html',
  styleUrls: ['./usersettings.component.css']
})
export class UsersettingsComponent implements OnInit {
  username: string;
  user: User;
  img: any;
  infoForm: FormGroup;
  invalidInfo: boolean;
  newPicture: any = null;
  passwordForm: FormGroup;
  invalidPassword: boolean;
  message: string = null;
  successMessage: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
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

      this.message = this.authenticationService.getMessage;
      this.successMessage = this.message != null && (this.message === 'Password changed successfully!' || this.message === 'Basic info changed successfully!');
      localStorage.removeItem('message');

      this.initForms();
    });
  }

  get f1(): { [p: string]: AbstractControl } {
    return this.infoForm.controls;
  }

  get f2(): { [p: string]: AbstractControl } {
    return this.passwordForm.controls;
  }

  checkRoleTouched(): boolean {
    console.log('host: ' + this.f1.isHost.touched + ' guest: ' + this.f1.isGuest.touched);
    return this.f1.isHost.touched || this.f1.isGuest.touched;
  }

  initForms(): void {
    this.infoForm = this.formBuilder.group({
      firstName: [this.user.firstName],
      lastName: [this.user.lastName],
      username: [this.user.username],
      email: [this.user.email, [Validators.email]],
      password: [this.user.password, [Validators.required, Validators.minLength(4)]],
      phone: [this.user.phone, [Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]],
      isAdmin: [this.user.isAdmin],
      isHost: [this.user.isHost],
      isGuest: [this.user.isGuest],
      profilePicture: [''],
    }, { validator: [checkRolesValidator] });
    this.newPicture = null;

    this.passwordForm = this.formBuilder.group({
      currentPassword: ['', [Validators.required, Validators.minLength(4)]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(4)]]
    }, { validator: [confirmPasswordValidator] });
  }

  onFileChange(event): void {
    if (event.target.files && event.target.files.length) {
      this.newPicture = event.target.files[0];

      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);

      reader.onload = (event) => { // called once readAsDataURL is completed
        this.img = event.target.result;
      };
    }
  }

  onInfoSubmit(): void {
    const jsonUser = JSON.stringify(this.infoForm.value, ((key, val) => {
      if (key === 'profilePicture') {
        return null;
      }
      else {
        return val;
      }
    }));

    const formData = new FormData();
    formData.append('user', jsonUser);
    formData.append('oldUsername', this.username);
    formData.append('oldEmail', this.f1.email.value);
    formData.append('profilePicture', this.newPicture);

    // console.log(formData.get('profilePicture'));
    // console.log(formData.get('user'));

    this.dataService.changeInfo(formData)
      .subscribe(
        response => {
          this.invalidInfo = false;
          this.authenticationService.changeCurrentUsername(this.f1.username.value);
          this.authenticationService.changeMessage('Basic info changed successfully!');
          window.location.reload();
        },
        (error: HttpErrorResponse) => {
          this.invalidInfo = true;
          this.authenticationService.changeMessage('Error: invalid username or email');
          window.location.reload();
        }
      );
  }

  onPasswordSubmit(): void {
    const formData = new FormData();
    formData.append('username', this.username);
    formData.append('currentPassword', this.f2.currentPassword.value);
    formData.append('newPassword', this.f2.password.value);

    this.dataService.changePassword(formData)
      .subscribe(
        response => {
          this.invalidPassword = false;
          this.authenticationService.changeMessage('Password changed successfully!');
          window.location.reload();
        },
        (error: HttpErrorResponse) => {
          this.invalidPassword = true;
          this.authenticationService.changeMessage('Error: can\'t confirm current password');
          window.location.reload();
        }
      );
  }

}

function confirmPasswordValidator(c: AbstractControl): { mismatch: boolean } {
  if (c.get('password').value !== c.get('confirmPassword').value) {
    return {mismatch: true};
  }
}

function checkRolesValidator(c: AbstractControl): { mismatch: boolean } {
  if (!c.get('isHost').value && !c.get('isGuest').value) {
    return {mismatch: true};
  }
}

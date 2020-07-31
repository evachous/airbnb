import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { DataService } from '../../services/data.service';
import { AlertService } from '../../services/alert.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  errorMessage: string;
  invalidSignup: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private dataService: DataService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(4)]],
      phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]],
      city: ['', Validators.required],
      country: ['', Validators.required],
      isAdmin: false,
      isHost: false,
      isGuest: false,
      isApproved: true,
      profilePicture: ['', Validators.required],
      }, { validator: [confirmPasswordValidator, checkRolesValidator] });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.signupForm.controls;
  }

  onFileChange(event): void {
    if (event.target.files && event.target.files.length) {
      const file = event.target.files[0];
      this.f.profilePicture.setValue(file);
    }
  }

  checkRoleTouched(): boolean {
    return this.f.isHost.touched || this.f.isGuest.touched;
  }

  onSubmit(): void {
    // convert form to json
    const jsonUser = JSON.stringify(this.signupForm.value, ((key, val) => {
      if (key === 'profilePicture') {
        return null;
      }
      else if (key === 'confirmPassword') {
        return undefined;
      }
      else if (key === 'isApproved') {
        return !this.f.isHost.value;
      }
      else {
        return val;
      }
    }));

    // send post request with json user + profile picture
    const formData = new FormData();
    formData.append('user', jsonUser);
    formData.append('profilePicture', this.f.profilePicture.value);

    this.dataService.signup(formData)
      .subscribe(
        response => {
          // window.alert('User signed up successfully!');
          if (!this.f.isHost.value) {
            this.alertService.changeMessage('User signed up successfully!');
          }
          else {
            this.alertService.changeMessage('Waiting approval from admin...');
          }
          this.invalidSignup = false;
          this.router.navigate(['/login']);
        },
        (error: HttpErrorResponse) => {
          this.errorMessage = 'Error signing up: invalid username or email';  // TODO: different error messages
          this.signupForm.reset();
          this.invalidSignup = true;
          this.router.navigate(['/signup']);
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

import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  returnUrl: string;
  loading = false;
  invalidLogin: boolean;
  message: string = null;
  successMessage: boolean;
  warningMessage = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
    this.findMessage();
    localStorage.removeItem('message');
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.loading = true;
    this.authenticationService.login(this.f.username.value, this.f.password.value)
      .subscribe(
        (response: any) => {
          this.authenticationService.setToken('token', response.headers.get('Authorization'));
          this.authenticationService.setToken('username', this.f.username.value);
          this.invalidLogin = false;
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.invalidLogin = true;
          this.alertService.changeMessage('Error logging in');
        }
      );
  }

  findMessage(): void {
    this.message = this.alertService.getMessage;
    if (this.message === 'User signed up successfully!') {
      this.successMessage = true;
    }
    else if (this.message === 'Error logging in') {
      this.successMessage = false;
    }
    else if (this.message === 'Waiting approval from admin...') {
      this.successMessage = false;
      this.warningMessage = true;
    }
  }
}

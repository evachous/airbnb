import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

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

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) { }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
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
          /*console.log('User Logged In Successfully');
          console.log(this.returnUrl);*/
          this.invalidLogin = false;
          this.router.navigate([this.returnUrl]);
        },
        error => {
          this.invalidLogin = true;
          console.log('ERROR');
        }
      );
  }
}

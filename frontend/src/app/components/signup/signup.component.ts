import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { DataService } from '../../services/data.service';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

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
    private dataService: DataService
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
      profilePicture: ['', Validators.required],
      isHost: [],
      isGuest: [],
      }, { validator: confirmPasswordValidator });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.signupForm.controls;
  }

  onFileChange(event): void {
    const reader = new FileReader();

    if (event.target.files && event.target.files.length) {
      const [file] = event.target.files;
      reader.readAsDataURL(file);

      reader.onload = () => {
        this.signupForm.patchValue({
          profilePicture: reader.result
        });
      };
    }
  }

  onSubmit(): void {
    this.dataService.signup(this.signupForm.value)
      .subscribe(
        response => {
          window.alert('User signed up successfully!');
          this.invalidSignup = false;
          this.router.navigate(['/login']);
        },
        (error: any) => {
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

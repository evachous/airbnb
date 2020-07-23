import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators, ValidatorFn, ValidationErrors, AbstractControl} from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }
  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password1: ['', [Validators.required, Validators.minLength(4)]],
      password2: ['', [Validators.required, Validators.minLength(4)]],
      phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]],
      profilePicture: ['', Validators.required],
      }, { validators: confirmPasswordValidator });
    // });
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
}

const confirmPasswordValidator: ValidatorFn = (control: FormGroup): null | ValidationErrors => {
  const p1 = control.get('password');
  const p2 = control.get('password1');

  return p1 && p2 && p1.value === p2.value ? null : { notSame: true };
};

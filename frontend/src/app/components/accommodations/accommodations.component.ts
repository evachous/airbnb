import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from "../../services/authentication.service";
import {User} from "../../model/user";
import {DataService} from "../../services/data.service";
import {Accommodation, AccommodationInfo} from "../../model/accommodation";
import {AbstractControl, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-accommodations',
  templateUrl: './accommodations.component.html',
  styleUrls: ['./accommodations.component.css']
})
export class AccommodationsComponent implements OnInit {
  username: string;
  user: User;
  accommodations: Accommodation[] = null;
  info: AccommodationInfo;
  emptyAcc: boolean;
  infoForm: FormGroup;
  locationForm: FormGroup;
  rulesForm: FormGroup;
  message: string = null;
  successMessage: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private dataService: DataService,
    private alertService: AlertService
  ) { }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.dataService.getUser(this.username).subscribe( user => {
      this.user = user;
      this.accommodations = this.user.accommodations;
      this.emptyAcc = this.accommodations.length == 0;

      this.message = this.alertService.getMessage;
      this.successMessage = this.message != null && (this.message === 'Added accommodation successfully!');
      localStorage.removeItem('message');

      this.initForms();
    });
  }

  get f1(): { [p: string]: AbstractControl } {
    return this.infoForm.controls;
  }

  get f2(): { [p: string]: AbstractControl } {
    return this.locationForm.controls;
  }

  get f3(): { [p: string]: AbstractControl } {
    return this.rulesForm.controls;
  }

  initForms(): void {
    this.infoForm = this.formBuilder.group({
      type: ['', Validators.required],
      beds: ['', Validators.required],
      bedrooms: ['', Validators.required],
      bathrooms: ['', Validators.required],
      livingRoom: false,
      area: ['', Validators.required],
      description: ['', Validators.required]
    });

    this.locationForm = this.formBuilder.group({
      address: ['', Validators.required],
      neighborhood: ['', Validators.required],
      transportation: ['', Validators.required]
    });

    this.rulesForm = this.formBuilder.group({
      smoking: false,
      pets: false,
      events: false,
      minDays: ['', Validators.required]
    });
  }

  onSubmit(): void {
    const jsonInfo = this.stringifyForm(this.infoForm);
    const jsonLocation = this.stringifyForm(this.locationForm);
    const jsonRules = this.stringifyForm(this.rulesForm);

    const formData = new FormData();
    formData.append('info', jsonInfo);
    formData.append('location', jsonLocation);
    formData.append('rules', jsonRules);
    formData.append('username', this.username);

    console.log(jsonInfo);
    console.log(jsonLocation);
    console.log(jsonRules);

    this.dataService.addAccommodation(formData)
      .subscribe(
        response => {
          this.alertService.changeMessage('Added accommodation successfully!');
          window.location.reload();
        },
        (error: HttpErrorResponse) => {
          this.alertService.changeMessage('Error adding accommodation');
          window.location.reload();
        }
      )
  }

  stringifyForm(form: FormGroup): string {
    return JSON.stringify(form.value);
  }
}

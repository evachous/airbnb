import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";
import {Reservation} from "../../model/reservation";
import {AuthenticationService} from "../../services/authentication.service";
import {DataService} from "../../services/data.service";
import {AbstractControl, Form, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpErrorResponse} from "@angular/common/http";
import {AlertService} from "../../services/alert.service";

@Component({
  selector: 'app-guestreservations',
  templateUrl: './guestreservations.component.html',
  styleUrls: ['./guestreservations.component.css']
})
export class GuestreservationsComponent implements OnInit {
  username: string;
  user: User;
  reservations: Reservation[] = new Array<Reservation>();
  emptyReserves: boolean;
  accommodationImages: string[] = new Array<string>();

  reviewForms: FormGroup[] = new Array<FormGroup>();

  message: string = null;
  successMessage: boolean;

  page = 1;
  pageSize = 20;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private alertService: AlertService,
    private dataService: DataService,
  ) { }

  ngOnInit(): void {
    this.username = this.authenticationService.getTokenUsername;
    this.dataService.getUser(this.username).subscribe( user => {
      this.user = user;

      this.message = this.alertService.getMessage;
      this.successMessage = this.message != null && (this.message === 'Added review successfully!');
      localStorage.removeItem('message');

      this.dataService.getGuestReservations(this.user.username)
        .subscribe(reservations => {
          this.reservations = reservations;
          this.emptyReserves = this.reservations.length == 0;

          for (let i = 0; i < this.reservations.length; i++) {
            if (!this.reservations[i].accommodation.images.length)
              this.accommodationImages[i] = 'http://placehold.it/150x150'
            else {
              this.dataService.getAccommodationImage(this.reservations[i].accommodation.id, 0)
                .subscribe(image => {
                  if (image === '')
                    this.accommodationImages[i] = 'http://placehold.it/150x150';
                  else
                    this.accommodationImages[i] = 'data:image/jpeg;base64,' + image;
                }, error => {
                  this.accommodationImages[i] = 'http://placehold.it/150x150';
                  console.log(error);
                })
            }

            this.initForm(i);
          }
        })
    })
  }

  initForm(i): void {
    if (this.reservations[i].review == null) {
      this.reviewForms[i] = this.formBuilder.group({
        text: ['', Validators.required],
        rating: ['', Validators.required]
      })
    }
    else {
      console.log('hello');
      this.reviewForms[i] = this.formBuilder.group({
        text: [this.reservations[i].review.text, Validators.required],
        rating: [this.reservations[i].review.rating, Validators.required]
      })
    }
  }

  onSubmit(i): void {
    const formData = new FormData();
    formData.append('reservationID', this.reservations[i].id.toString());
    formData.append('review', JSON.stringify(this.reviewForms[i].value));

    this.dataService.addReview(formData)
      .subscribe(response => {
        this.alertService.changeMessage('Added review successfully!');
        window.location.reload();
    },(error: HttpErrorResponse) => {
        this.alertService.changeMessage('Error adding review');
        window.location.reload();
      })
  }

}

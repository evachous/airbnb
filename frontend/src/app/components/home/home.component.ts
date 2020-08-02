import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NgbDate, NgbCalendar} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  searchForm: FormGroup;
  hoveredDate: NgbDate | null = null;
  minDate: NgbDate;
  invalidDate: boolean;
  errorMessage: string;

  constructor(
    private formBuilder: FormBuilder,
    private dataService: DataService,
    private calendar: NgbCalendar
  ) {
    this.minDate = calendar.getToday();
  }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      location: ['', Validators.required],
      checkin: [null, Validators.required],
      checkout: [null, Validators.required],
      guests: ['', Validators.required]
    });
  }

  get f(): { [p: string]: AbstractControl } {
    return this.searchForm.controls;
  }

  onDateSelection(): void {
    const date = this.f.checkout.value;
    this.invalidDate = false;
    if (this.f.checkout.value && this.f.checkin.value && !date.after(this.f.checkin.value)) {
      this.invalidDate = true;
      this.errorMessage = 'Invalid check out date!';
      this.f.checkout.setValue(null);
    }
  }

  isHovered(date: NgbDate): boolean {
    return this.f.checkin.value && !this.f.checkout.value && this.hoveredDate && date.after(this.f.checkin.value)
      && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate): boolean {
    return this.f.checkout.value && date.after(this.f.checkin.value) && date.before(this.f.checkout.value);
  }

  isRange(date: NgbDate): boolean {
    return date.equals(this.f.checkin.value) || (this.f.checkout.value && date.equals(this.f.checkout.value))
      || this.isInside(date) || this.isHovered(date);
  }

  onSubmit(): void {
    console.log(this.f.checkin.value);
    console.log(this.f.checkout.value);
  }
}

import { Component, OnInit } from '@angular/core';
import { DataService } from '../../services/data.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  searchForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private dataService: DataService
  ) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      location: ['', Validators.required],
      checkin: ['', Validators.required],
      checkout: ['', Validators.required],
      guests: ['', Validators.required]
    });
  }

}

import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {DataService} from "../../services/data.service";
import {Accommodation} from "../../model/accommodation";

@Component({
  selector: 'app-accommodation',
  templateUrl: './accommodation.component.html',
  styleUrls: ['./accommodation.component.css']
})
export class AccommodationComponent implements OnInit {
  found = true;
  accommodationID: number;
  accommodation: Accommodation;
  accommodationImages: string[] = new Array<string>();

  constructor(
    private route: ActivatedRoute,
    private dataService: DataService,
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.accommodationID = params.id;

      this.dataService.getAccommodation(this.accommodationID).subscribe(acc => {
        this.accommodation = acc;
        this.found = true;

        for (let i = 0; i < this.accommodation.images.length; i++) {
          this.dataService.getAccommodationImage(this.accommodationID, i).subscribe(image => {
            this.accommodationImages[i] = 'data:image/jpeg;base64,' + image;
            console.log(this.accommodationImages[i]);
          })
        }
      },
        error => {
          this.found = false;
        })
    });
  }

}

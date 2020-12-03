import {Component, HostListener, OnInit} from '@angular/core';
import {Browser} from "leaflet";
import win = Browser.win;

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  scrollable: boolean;

  constructor() { }

  ngOnInit(): void {
    this.scrollable = document.documentElement.scrollHeight > document.documentElement.clientHeight;
    //this.scrollable = window.scrollbars.visible;
    console.log("scrollable " + this.scrollable + " " + window.innerHeight + " " +
      document.documentElement.clientHeight + " " + document.documentElement.offsetHeight);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    //this.scrollable = true;
    console.log("resize " + event.target.innerHeight + " " + this.scrollable);
  }
}

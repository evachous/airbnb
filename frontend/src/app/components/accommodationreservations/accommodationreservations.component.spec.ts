import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AccommodationreservationsComponent } from './accommodationreservations.component';

describe('AccommodationreservationsComponent', () => {
  let component: AccommodationreservationsComponent;
  let fixture: ComponentFixture<AccommodationreservationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AccommodationreservationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccommodationreservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

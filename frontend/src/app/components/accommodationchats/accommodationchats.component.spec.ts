import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AccommodationchatsComponent } from './accommodationchats.component';

describe('AccommodationchatsComponent', () => {
  let component: AccommodationchatsComponent;
  let fixture: ComponentFixture<AccommodationchatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AccommodationchatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccommodationchatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

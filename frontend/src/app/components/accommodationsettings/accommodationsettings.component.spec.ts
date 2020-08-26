import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AccommodationsettingsComponent } from './accommodationsettings.component';

describe('AccommodationsettingsComponent', () => {
  let component: AccommodationsettingsComponent;
  let fixture: ComponentFixture<AccommodationsettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AccommodationsettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccommodationsettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GuestreservationsComponent } from './guestreservations.component';

describe('GuestreservationsComponent', () => {
  let component: GuestreservationsComponent;
  let fixture: ComponentFixture<GuestreservationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GuestreservationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuestreservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HostreservationsComponent } from './hostreservations.component';

describe('HostreservationsComponent', () => {
  let component: HostreservationsComponent;
  let fixture: ComponentFixture<HostreservationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HostreservationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostreservationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GuestchatsComponent } from './guestchats.component';

describe('GuestchatsComponent', () => {
  let component: GuestchatsComponent;
  let fixture: ComponentFixture<GuestchatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GuestchatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GuestchatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

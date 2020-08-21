import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HostaccommodationsComponent } from './hostaccommodations.component';

describe('AccommodationsComponent', () => {
  let component: HostaccommodationsComponent;
  let fixture: ComponentFixture<HostaccommodationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HostaccommodationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostaccommodationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

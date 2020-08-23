import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HostchatsComponent } from './hostchats.component';

describe('HostchatsComponent', () => {
  let component: HostchatsComponent;
  let fixture: ComponentFixture<HostchatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HostchatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HostchatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

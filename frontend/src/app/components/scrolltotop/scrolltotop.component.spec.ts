import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ScrolltotopComponent } from './scrolltotop.component';

describe('ScrolltotopComponent', () => {
  let component: ScrolltotopComponent;
  let fixture: ComponentFixture<ScrolltotopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ScrolltotopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ScrolltotopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

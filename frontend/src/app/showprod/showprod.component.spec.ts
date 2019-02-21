import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowprodComponent } from './showprod.component';

describe('ShowprodComponent', () => {
  let component: ShowprodComponent;
  let fixture: ComponentFixture<ShowprodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowprodComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowprodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

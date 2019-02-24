import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProdShowComponent } from './prod-show.component';

describe('ProdShowComponent', () => {
  let component: ProdShowComponent;
  let fixture: ComponentFixture<ProdShowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProdShowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProdShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

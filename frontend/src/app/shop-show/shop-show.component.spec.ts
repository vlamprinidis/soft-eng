import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopShowComponent } from './shop-show.component';

describe('ShopShowComponent', () => {
  let component: ShopShowComponent;
  let fixture: ComponentFixture<ShopShowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShopShowComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopShowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

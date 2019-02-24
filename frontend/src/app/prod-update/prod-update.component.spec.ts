import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProdUpdateComponent } from './prod-update.component';

describe('ProdUpdateComponent', () => {
  let component: ProdUpdateComponent;
  let fixture: ComponentFixture<ProdUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProdUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProdUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

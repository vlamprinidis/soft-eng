import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProdNewComponent } from './prod-new.component';

describe('ProdNewComponent', () => {
  let component: ProdNewComponent;
  let fixture: ComponentFixture<ProdNewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProdNewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProdNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

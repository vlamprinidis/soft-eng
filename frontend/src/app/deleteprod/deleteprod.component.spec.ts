import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteprodComponent } from './deleteprod.component';

describe('DeleteprodComponent', () => {
  let component: DeleteprodComponent;
  let fixture: ComponentFixture<DeleteprodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeleteprodComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteprodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

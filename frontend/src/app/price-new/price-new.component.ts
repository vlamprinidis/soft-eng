import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../data.service';

@Component({
  selector: 'app-price-new',
  templateUrl: './price-new.component.html',
  styleUrls: ['./price-new.component.scss']
})
export class PriceNewComponent implements OnInit {

  pric: Object;
  messageForm: FormGroup;
  submitted = false;
  success = false;
  productId: number;
  shopId: number;
  myprods: Object;
  myshops: Object;

  constructor(private formBuilder: FormBuilder, private data: DataService) { }

  ngOnInit() {
    this.messageForm = this.formBuilder.group({
      price: ['', Validators.min(0)],
      dateFrom: [''],
      dateTo: [''],
    });
    this.data.getProducts('id|ASC', 'ACTIVE' ).subscribe(data => {
        this.myprods = data;
        console.log(this.myprods);
      }
    );
    this.data.getShops('id|ASC', 'ACTIVE' ).subscribe(data => {
        this.myshops = data;
        console.log(this.myshops);
      }
    );
    // this.productId = this.myprods. Get default value
  }

  onProdSelect(event) {
    this.productId = event.target.value;
    console.log(this.productId);
  }

  onShopSelect(event) {
    this.shopId = event.target.value;
    console.log(this.shopId);
  }

  onSubmit() {
    this.submitted = true;

    if (this.messageForm.invalid) {
      return;
    }

    this.success = true;
    this.data.addPrice(this.messageForm.controls.price.value, this.messageForm.controls.dateFrom.value,
      this.messageForm.controls.dateTo.value, this.productId, this.shopId).subscribe(data => {
        this.pric = data;
        console.log(this.pric);
      }
    );


  }

}

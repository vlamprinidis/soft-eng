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
  productId = -1;
  shopId = -1;
  myprods: Object;
  myshops: Object;

  constructor(private formBuilder: FormBuilder, private data: DataService) { }

  ngOnInit() {
    this.messageForm = this.formBuilder.group({
      price: ['', Validators.required],
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
    if (this.productId === -1 || this.shopId === -1) {
      alert('Πρέπει να επιλέξεις προϊόν και κατάστημα υποχρεωτικά');
      return;
    }

    if (isNaN(parseFloat(this.messageForm.controls.price.value))) {
      alert('Η τιμή πρέπει να είναι αριθμός');
      return;
    }

    if (this.messageForm.controls.price.value.includes(',')) {
      alert('Παρακαλώ χρησιμοποίησε "." (τελεία) για υποδιαστολή');
      return;
    }

    this.success = true;
    this.data.addPrice(this.messageForm.controls.price.value,
      this.messageForm.controls.dateTo.value, this.productId, this.shopId).subscribe(data => {
        this.pric = data;
        console.log(this.pric);
      }
    );


  }

}

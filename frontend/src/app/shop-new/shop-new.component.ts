import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../data.service';


@Component({
  selector: 'app-shop-new',
  templateUrl: './shop-new.component.html',
  styleUrls: ['./shop-new.component.scss']
})
export class ShopNewComponent implements OnInit {

  shop: Object;
  messageForm: FormGroup;
  submitted = false;
  success = false;


  constructor(private formBuilder: FormBuilder, private data: DataService) { }

  ngOnInit() {
    this.messageForm = this.formBuilder.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      lng: ['', Validators.required],
      lat: ['', Validators.required],
      tags: ['', Validators.required]
    });
  }

  ShowClick(id) {
    console.log('clicked');
    this.data.getShop(id).subscribe(data => {
        this.shop = data;
        console.log(this.shop);
      }
    );
  }

  onSubmit() {
    this.submitted = true;

    if (this.messageForm.invalid) {
      return;
    }

    this.success = true;
    this.data.addShop(this.messageForm.controls.name.value, this.messageForm.controls.address.value, this.messageForm.controls.lng.value,
      this.messageForm.controls.lat.value, this.messageForm.controls.tags.value).subscribe(data => {
        this.shop = data;
        console.log(this.shop);
      }
    );

  }

}

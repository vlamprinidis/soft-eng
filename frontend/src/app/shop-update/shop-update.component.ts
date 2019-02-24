import { Component, OnInit, OnDestroy } from '@angular/core';
import { DataService } from '../data.service';
import { ActivatedRoute, Router } from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-shop-update',
  templateUrl: './shop-update.component.html',
  styleUrls: ['./shop-update.component.scss']
})
export class ShopUpdateComponent implements OnInit, OnDestroy {
  id: number;
  name = '';
  address = '';
  lng = '';
  lat = '';
  withdrawn = false;
  tags = '';
  private sub: any;
  shop: Object;
  messageForm: FormGroup;
  submitted = false;
  success = false;

  constructor(private data: DataService, private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.sub = this.route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.id = +params['d1'] || 0;
        this.name = params['d2'] || '';
        this.address = params['d3'] || '';
        this.lng = params['d4'] || '';
        this.lat = params['d5'] || '';
        this.withdrawn = params['d7'] || false;
        this.tags = params['d6'] || '';
        console.log(this.id);
        console.log(this.name);
        console.log(this.address);
        console.log(this.lng);
        console.log(this.lat);
        console.log(this.withdrawn);
        console.log(this.tags);
      });
    this.messageForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.required],
      address: ['', Validators.required],
      lng: ['', Validators.required],
      lat: ['', Validators.required],
      withdrawn: [''],
      tags: ['', Validators.required]
    });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  onSubmit() {
    this.submitted = true;
    if (this.messageForm.invalid) {
      return;
    }
    this.success = true;
    this.data.updateShop(this.messageForm.controls.name.value, this.messageForm.controls.address.value, this.messageForm.controls.lng.value,
      this.messageForm.controls.lat.value, this.messageForm.controls.tags.value, this.id).subscribe(data => {
        this.shop = data;
        console.log(this.shop);
      }
    );
  }

}

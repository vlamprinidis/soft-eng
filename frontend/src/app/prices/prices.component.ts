import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import {FormBuilder, FormGroup } from '@angular/forms';

import OlMap from 'ol/Map';
import OlXYZ from 'ol/source/XYZ';
import OlTileLayer from 'ol/layer/Tile';
import OlView from 'ol/View';
import { fromLonLat } from 'ol/proj';
import * as proj from 'ol/proj';

import { Options } from 'ng5-slider';


@Component({
  selector: 'app-prices',
  templateUrl: './prices.component.html',
  styleUrls: ['./prices.component.scss']
})

export class PricesComponent  implements OnInit {

  messageForm: FormGroup;
  submitted = false;
  success = false;
  myprods: Object;
  myshops: Object;
  cntprod = 0;
  cntshop = 0;

  slidervalue: number = 1;
  options: Options = {
    floor: 1,
    ceil: 12,
    showTicks: true,
    stepsArray: [
      {value: 1, legend: '1 ώρα'},
      {value: 2, legend: '2 ώρες'},
      {value: 3, legend: '3 ώρες'},
      {value: 4, legend: '4 ώρες'},
      {value: 5, legend: '5 ώρες'},
      {value: 6, legend: '6 ώρες'},
      {value: 7, legend: '7 ώρες'},
      {value: 8, legend: '8 ώρες'},
      {value: 9, legend: '9 ώρες'},
      {value: 10, legend: '10 ώρες'},
      {value: 11, legend: '1 ημέρα'},
      {value: 12, legend: '1 μήνα'},
    ]
  };

  constructor(private formBuilder: FormBuilder, private data: DataService) {}

  sort = ['Απόσταση αύξουσα', 'Απόσταση φθίνουσα', 'Τιμή αύξουσα', 'Τιμή φθίνουσα', 'Ημερομηνία αύξουσα', 'Ημερομηνία φθίνουσα'];
  getsort = 'price|ASC';
  products = '';
  shops = '';
  pric: Object;
  prodfirst = '';
  prodlast = '';
  shopfirst = '';
  shoplast = '';

  // MAP STUFF
  latitude = 37.9758788;
  longitude = 23.7353989;
  map: OlMap;
  source: OlXYZ;
  layer: OlTileLayer;
  view: OlView;

  ngOnInit() {
    this.messageForm = this.formBuilder.group({
      tags: [''],
      geoDist: [''],
      geoLng: [''],
      geoLat: [''],
      dateFrom: [''],
      dateTo: ['']
      /*}, { validator: DistanceRequired })*/
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
    this.data.getPrices(this.getsort, '', '', '', '', '', '', '', '' ).subscribe(data => {
        this.pric = data;
        console.log(this.pric);
      }
    );

    // MAP CODE
    this.source = new OlXYZ({
      url: 'http://tile.osm.org/{z}/{x}/{y}.png'
    });

    this.layer = new OlTileLayer({
      source: this.source
    });

    this.view = new OlView({
      center: fromLonLat([this.longitude, this.latitude]),
      zoom: 15
    });

    this.map = new OlMap({
      target: 'map',
      layers: [this.layer],
      view: this.view
    });

    this.map.on('click', function (args) {
      console.log(args.coordinate);
      const lonlat = proj.transform(args.coordinate, 'EPSG:3857', 'EPSG:4326');
      console.log(lonlat);

      const lon = lonlat[0];
      const lat = lonlat[1];
      alert(`lat: ${lat} long: ${lon}`);
    });

  }

  onProdSelect(id, event) {
    // console.log(event.target.value);
    if (this.cntprod === 0) {
      this.products = event.target.value;
      this.prodfirst = event.target.value;
      this.cntprod ++;
    } else {
      if (!this.products.includes(',' + event.target.value + ',') && event.target.value !== this.prodfirst && event.target.value !== this.prodlast) {
          this.products = this.products + ',' + event.target.value;
          this.prodlast = event.target.value;
          this.cntprod ++;
      } else if (this.products.includes(',' + event.target.value + ',')) {
          this.products = this.products.replace(',' + event.target.value + ',', ',');
          this.cntprod --;
      } else if (event.target.value === this.prodfirst) {
          if (this.cntprod === 1) {
            this.products = this.products.replace(event.target.value, '');
          } else {
            this.products = this.products.replace(event.target.value + ',', '');
          }
          // console.log('begin ' + this.shops);
          const str_array = this.products.split(',');
          this.prodfirst = str_array[0];
          this.cntprod --;
      } else {
          this.products = this.products.replace(',' + event.target.value, '');
          const str_array = this.products.split(',');
          this.prodlast = str_array[str_array.length - 1];
          this.cntprod --;
      }
    }
    console.log(this.products);

  }

  onShopSelect(id, event) {
    // console.log(event.target.value);
    if (this.cntshop === 0) {
      this.shops = event.target.value;
      this.shopfirst = event.target.value;
      this.cntshop ++;
    } else {
      if (!this.shops.includes(',' + event.target.value + ',') && event.target.value !== this.shopfirst && event.target.value !== this.shoplast) {
        this.shops = this.shops + ',' + event.target.value;
        this.shoplast = event.target.value;
        this.cntshop ++;
      } else if (this.shops.includes(',' + event.target.value + ',')) {
        this.shops = this.shops.replace(',' + event.target.value + ',', ',');
        this.cntshop --;
      } else if (event.target.value === this.shopfirst) {
        if (this.cntshop === 1) {
          this.shops = this.shops.replace(event.target.value, '');
        } else {
          this.shops = this.shops.replace(event.target.value + ',', '');
        }
        // console.log('begin ' + this.shops);
        const str_array = this.shops.split(',');
        this.shopfirst = str_array[0];
        this.cntshop --;
      } else {
        this.shops = this.shops.replace(',' + event.target.value, '');
        const str_array = this.shops.split(',');
        this.shoplast = str_array[str_array.length - 1];
        this.cntshop --;
      }
    }
    console.log(this.shops);

  }

  onSortSelect(event) {
    console.log(event.target.value);
    const temp = event.target.value;
    if (temp === 'Απόσταση αύξουσα') {
      this.getsort = 'geoDist|ASC';
    } else if (temp === 'Απόσταση φθίνουσα') {
      this.getsort = 'geoDist|DESC';
    } else if (temp === 'Τιμή αύξουσα') {
      this.getsort = 'price|ASC';
    } else if (temp === 'Τιμή φθίνουσα') {
      this.getsort = 'price|DESC';
    } else if (temp === 'Ημερομηνία αύξουσα') {
      this.getsort = 'date|ASC';
    } else {
      this.getsort = 'date|DESC';
    }
  }

  refresh() {
    window.location.reload();
  }

  onSubmit() {
    this.submitted = true;

    if (((this.messageForm.controls.geoDist.value === '' && this.messageForm.controls.geoLng.value === '' && this.messageForm.controls.geoLat.value === '')
    || (this.messageForm.controls.geoDist.value !== '' && this.messageForm.controls.geoLng.value !== '' && this.messageForm.controls.geoLat.value !== '')) &&
      ((this.messageForm.controls.dateFrom.value === '' && this.messageForm.controls.dateTo.value === '' )
        || (this.messageForm.controls.dateFrom.value !== '' && this.messageForm.controls.dateTo.value !== '' ))) {

      this.success = true;
      console.log(this.messageForm.controls.tags.value);
      this.data.getPrices(this.getsort, this.messageForm.controls.geoDist.value, this.messageForm.controls.geoLng.value,
        this.messageForm.controls.geoLat.value, this.messageForm.controls.dateFrom.value, this.messageForm.controls.dateTo.value,
        this.products, this.shops, this.messageForm.controls.tags.value ).subscribe
      (data => {
          this.pric = data;
          console.log(this.pric);
        }
      );
    } else {
      alert('Τα πεδία της απόστασης και της ημερομηνίας πρέπει να συμπληρώνονται είτε όλα είτε κανένα');
    }
  }


}

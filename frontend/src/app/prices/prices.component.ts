import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import { FormBuilder, FormGroup } from '@angular/forms';

import OlMap from 'ol/Map';
import OlXYZ from 'ol/source/XYZ';
import OlTileLayer from 'ol/layer/Tile';
import OlView from 'ol/View';
import { fromLonLat } from 'ol/proj';
import * as proj from 'ol/proj';

// import { Options } from 'ng5-slider';


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
  cntsort = 0;

  /*slidervalue = 0;
  options: Options = {
    floor: 0,
    ceil: 12,
    showTicks: true,
    stepsArray: [
      {value: 0, legend: 'όλα'},
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
  };*/

  constructor(private formBuilder: FormBuilder, private data: DataService) {}

  sort = ['Απόσταση αύξουσα', 'Απόσταση φθίνουσα', 'Τιμή αύξουσα', 'Τιμή φθίνουσα', 'Ημερομηνία αύξουσα', 'Ημερομηνία φθίνουσα'];
  getsort = 'price|ASC';
  products = '';
  shops = '';
  dist = '';
  tags = '';
  datefrom = '';
  dateto = '';
  pric: Object;
  prodfirst = '';
  prodlast = '';
  shopfirst = '';
  shoplast = '';
  sortfirst = '';
  sortlast = '';

  // MAP STUFF
  latitude = 37.9758788;
  longitude = 23.7353989;
  map: OlMap;
  source: OlXYZ;
  layer: OlTileLayer;
  view: OlView;
  lng = '';
  lat = '';

  ngOnInit() {
    this.messageForm = this.formBuilder.group({
      tags: [''],
      geoDist: [''],
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

  /*onSortSelect(event) {
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
  }*/

  onSortSelect(id,event) {
    // console.log(event.target.value);
    let temp = event.target.value;
    if (temp === 'Απόσταση αύξουσα') {
      temp = 'geoDist|ASC';
    } else if (temp === 'Απόσταση φθίνουσα') {
      temp = 'geoDist|DESC';
    } else if (temp === 'Τιμή αύξουσα') {
      temp = 'price|ASC';
    } else if (temp === 'Τιμή φθίνουσα') {
      temp = 'price|DESC';
    } else if (temp === 'Ημερομηνία αύξουσα') {
      temp = 'date|ASC';
    } else {
      temp = 'date|DESC';
    }

    if (this.cntsort === 0) {
      this.getsort = temp;
      this.sortfirst = temp;
      this.cntsort ++;
    } else {
      if (!this.getsort.includes(',' + temp + ',') && temp !== this.sortfirst && temp !== this.sortlast) {
        this.getsort = this.getsort + ',' + temp;
        this.sortlast = temp;
        this.cntsort ++;
      } else if (this.getsort.includes(',' + temp + ',')) {
        this.getsort = this.getsort.replace(',' + temp + ',', ',');
        this.cntsort --;
      } else if (temp === this.sortfirst) {
        if (this.cntsort === 1) {
          this.getsort = this.getsort.replace(temp, '');
        } else {
          this.getsort = this.getsort.replace(temp + ',', '');
        }
        // console.log('begin ' + this.shops);
        const str_array = this.getsort.split(',');
        this.sortfirst = str_array[0];
        this.cntsort --;
      } else {
        this.getsort = this.getsort.replace(',' + temp, '');
        const str_array = this.getsort.split(',');
        this.sortlast = str_array[str_array.length - 1];
        this.cntsort --;
      }
    }
    console.log(this.getsort);
    this.data.getPrices(this.getsort, this.dist, this.lng, this.lat, this.datefrom, this.dateto, this.products, this.shops, this.tags ).subscribe(data => {
        this.pric = data;
        console.log(this.pric);
      }
    );
  }

  refresh() {
    window.location.reload();
  }

  onSubmit() {
    if (this.messageForm.controls.geoDist.value.includes(',')) {
      alert('Παρακαλώ χρησιμοποίησε "." (τελεία) για υποδιαστολή');
      return;
    }

    const coordinates = this.map.getView().getCenter();
    const lonlat = proj.transform(coordinates, 'EPSG:3857', 'EPSG:4326');
    console.log(lonlat);

    this.lng = lonlat[0];
    this.lat = lonlat[1];
    // alert(`lat: ${this.lat} long: ${this.lng}`);
    this.submitted = true;

    /*(((this.messageForm.controls.geoDist.value === '' && this.lng === '' && this.lat === '')
    || (this.messageForm.controls.geoDist.value !== '' && this.lng !== '' && this.lat !== '')) &&*/
    if ((this.messageForm.controls.dateFrom.value === '' && this.messageForm.controls.dateTo.value === '' )
        || (this.messageForm.controls.dateFrom.value !== '' && this.messageForm.controls.dateTo.value !== '' )) {
      if (this.messageForm.controls.geoDist.value === '') {
        this.lng = '';
        this.lat = '';
      }

      this.dist = this.messageForm.controls.geoDist.value;
      this.datefrom = this.messageForm.controls.dateFrom.value;
      this.dateto = this.messageForm.controls.dateTo.value;
      this.tags = this.messageForm.controls.tags.value;

      this.success = true;
      console.log(this.messageForm.controls.tags.value);
      this.data.getPrices(this.getsort, this.messageForm.controls.geoDist.value, this.lng,
        this.lat, this.messageForm.controls.dateFrom.value, this.messageForm.controls.dateTo.value,
        this.products, this.shops, this.messageForm.controls.tags.value ).subscribe
      (data => {
          this.pric = data;
          console.log(this.pric);
        }
      );
    } else {
      alert('Τα πεδία της ημερομηνίας πρέπει να συμπληρώνονται είτε όλα είτε κανένα');
    }
  }


}

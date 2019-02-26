import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import {FormBuilder, FormGroup, Validators, FormControl, AbstractControl, } from '@angular/forms';


/*const DistanceRequired = (control: AbstractControl) => {
  const max = control.get('maxim');
  const lng = control.get('lng');
  const lat = control.get('lat');
  const values = [
    max.value,
    lng.value,
    lat.value
  ];
  if (values.every(x => x === '') || values.every(x => x !== '')) {
    return null;
  } else {
    return {DistanceIncomplete: true};
  }
}; */

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
  latitude: number = 37.9758788;
  longitude: number = 23.7353989;
  map: any;
  ol: any;

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
    this.map = new ol.Map({
      target: 'map',
      layers: [
        new ol.layer.Tile({
          source: new ol.source.OSM()
        })
      ],
      view: new ol.View({
        center: ol.proj.fromLonLat([this.longitude, this.latitude]),
        zoom: 16
      })
    });

    this.map.on('click', function (args) {
      console.log(args.coordinate);
      const lonlat = ol.proj.transform(args.coordinate, 'EPSG:3857', 'EPSG:4326');
      console.log(lonlat);

      const lon = lonlat[0];
      const lat = lonlat[1];
      alert(`lat: ${lat} long: ${lon}`);
    });

  }

  /* onProdSelect(event) {
     console.log(event.target.value);
     if(this.cntprod === 0){
       this.products = event.target.value;
       this.prodfirst = event.target.value;
     } else {
       if(!this.products.includes(','+ event.target.value + ',') && event.target.value !== this.prodfirst && event.target.value !== this.prodlast){
         this.products = this.products + ',' + event.target.value;
         this.prodlast = event.target.value;
       }
     }
     this.cntprod ++;
     console.log(this.products);
   }

   onShopSelect(event) {
     console.log(event.target.value);
     if(this.cntshop === 0){
       this.shops = event.target.value;
       this.shopfirst = event.target.value;
     } else {
       if(!this.shops.includes(','+ event.target.value + ',') && event.target.value !== this.shopfirst && event.target.value !== this.shoplast){
         this.shops = this.shops + ',' + event.target.value;
         this.shoplast = event.target.value;
       }
     }
     this.cntshop ++;
     console.log(this.shops);
   }*/

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

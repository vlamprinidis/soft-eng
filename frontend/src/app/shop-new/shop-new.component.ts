import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DataService} from '../data.service';

import OlMap from 'ol/Map';
import OlXYZ from 'ol/source/XYZ';
import OlTileLayer from 'ol/layer/Tile';
import OlView from 'ol/View';
import { fromLonLat } from 'ol/proj';
import * as proj from 'ol/proj';

// declare let LongLat;

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

  // MAP STUFF
  latitude = 37.9758788;
  longitude = 23.7353989;
  map: OlMap;
  source: OlXYZ;
  layer: OlTileLayer;
  view: OlView;
  lng;
  lat;

  constructor(private formBuilder: FormBuilder, private data: DataService) { }

  ngOnInit() {
    this.messageForm = this.formBuilder.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      // lng: ['', Validators.required],
      // lat: ['', Validators.required],
      tags: ['', Validators.required]
    });

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

  onSubmit() {
    const coordinates = this.map.getView().getCenter();
    const lonlat = proj.transform(coordinates, 'EPSG:3857', 'EPSG:4326');
    console.log(lonlat);

    this.lng = lonlat[0];
    this.lat = lonlat[1];

    this.submitted = true;

    if (this.messageForm.invalid) {
      return;
    }

    this.success = true;
    this.data.addShop(this.messageForm.controls.name.value, this.messageForm.controls.address.value, this.lng, this.lat,
      this.messageForm.controls.tags.value).subscribe(data => {
        this.shop = data;
        console.log(this.shop);
      }
    );

  }

}

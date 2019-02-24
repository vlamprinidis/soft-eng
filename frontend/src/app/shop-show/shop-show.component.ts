import {Component, OnDestroy, OnInit} from '@angular/core';
import {DataService} from '../data.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-shopshow',
  templateUrl: './shop-show.component.html',
  styleUrls: ['./shop-show.component.scss']
})
export class ShopShowComponent implements OnInit, OnDestroy {

  id: number;
  name = '';
  address = '';
  lng = '';
  lat = '';
  withdrawn = false;
  tags = '';
  private sub: any;

  constructor(private data: DataService, private route: ActivatedRoute, private router: Router) { }

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
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}

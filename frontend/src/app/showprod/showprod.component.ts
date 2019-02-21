import { Component, OnInit , OnDestroy } from '@angular/core';
import { DataService } from '../data.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-showprod',
  templateUrl: './showprod.component.html',
  styleUrls: ['./showprod.component.scss']
})
export class ShowprodComponent implements OnInit, OnDestroy {

  id: number;
  name = '';
  description = '';
  category = '';
  withdrawn = false;
  tags = '';
  private sub: any;
  post = false;

  constructor(private data: DataService, private route: ActivatedRoute, private router: Router) { }

  /*ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.prod = +params['prod']; // (+) converts string 'id' to a number
      // In a real app: dispatch action to load the details here.
    });
  }*/

  ngOnInit() {
    this.sub = this.route
      .queryParams
      .subscribe(params => {
        // Defaults to 0 if no query param provided.
        this.id = +params['d1'] || 0;
        this.name = params['d2'] || '';
        this.description = params['d3'] || '';
        this.category = params['d4'] || '';
        this.withdrawn = params['d6'] || false;
        this.tags = params['d5'] || '';
        console.log(this.id);
        console.log(this.name);
        console.log(this.description);
        console.log(this.category);
        console.log(this.withdrawn);
        console.log(this.tags);
      });
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  /*ShowClick(prod) {
    console.log('clicked');
    this.data.getProduct(this.id).subscribe(data => {
        this.prod = data;
        console.log(this.prod);
      }
    );
  }*/

}

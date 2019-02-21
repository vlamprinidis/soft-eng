import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  // h1Style: boolean = false;
  prods: Object;
  submitted = false;

  constructor(private data: DataService) { }

  status = [ 'Ενεργά', 'Όλα', 'Ανενεργά' ];
  sort = ['id φθίνον', 'id αύξον', 'όνομα αύξον', 'όνομα φθίνον'];
  getsort = 'id|DESC';
  getstatus = 'ACTIVE';


  ngOnInit() {
    this.data.getProducts(this.getsort, this.getstatus ).subscribe(data => {
        this.prods = data;
        console.log(this.prods);
      }
    );

  }

  onStatusSelect(event) {
    console.log(event.target.value);
    const temp = event.target.value;
    if (temp === 'Ενεργά') {
      this.getstatus = 'ACTIVE';
    } else if (temp === 'Όλα') {
      this.getstatus = 'ALL';
    } else {
      this.getstatus = 'WITHDRAWN';
    }
    this.data.getProducts(this.getsort, this.getstatus ).subscribe(data => {
        this.prods = data;
        console.log(this.prods);
      }
    );
  }

  onSortSelect(event) {
    console.log(event.target.value);
    const temp = event.target.value;
    if (temp === 'id φθίνον') {
      this.getsort = 'id|DESC';
    } else if (temp === 'id αύξον') {
      this.getsort = 'id|ASC';
    } else if (temp === 'όνομα αύξον') {
      this.getsort = 'name|ASC';
    } else {
      this.getsort = 'name|DESC';
    }
    this.data.getProducts(this.getsort, this.getstatus ).subscribe(data => {
        this.prods = data;
        console.log(this.prods);
      }
    );
  }

  firstClick() {
    this.data.firstClick();
  }

  onSubmit() {
    this.submitted = true;
    this.data.getProducts('id|ASC', 'ALL' ).subscribe(data => {
        this.prods = data;
        console.log(this.prods);
      }
    );
  }

}

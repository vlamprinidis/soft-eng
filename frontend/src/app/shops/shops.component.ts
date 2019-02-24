import { Component, OnInit } from '@angular/core';
import { DataService } from '../data.service';
import {Observable} from 'rxjs';


@Component({
  selector: 'app-shops',
  templateUrl: './shops.component.html',
  styleUrls: ['./shops.component.scss']
})

export class ShopsComponent implements OnInit {
  shops: Object;
  shop: Object;
  submitted = false;
  delmes: Object;

  constructor(private data: DataService) { }

  status = [ 'Ενεργά', 'Όλα', 'Ανενεργά' ];
  sort = ['id φθίνον', 'id αύξον', 'όνομα αύξον', 'όνομα φθίνον'];
  getsort = 'id|DESC';
  getstatus = 'ACTIVE';


  ngOnInit() {
    this.data.getShops (this.getsort, this.getstatus ).subscribe(data => {
        this.shops = data;
        console.log(this.shops);
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
    this.data.getShops(this.getsort, this.getstatus ).subscribe(data => {
        this.shops = data;
        console.log(this.shops);
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
    this.data.getShops(this.getsort, this.getstatus ).subscribe(data => {
        this.shops = data;
        console.log(this.shops);
      }
    );
  }

  ShowClick(id) {
    console.log('clicked');
  }

  DeleteClick(id) {
    console.log('clicked');
    if(confirm("Είσαι σίγουρος ότι θέλεις να διαγράψεις αυτό το κατάστημα;")) {
      this.data.deleteShop(id).subscribe(data => {
          this.delmes = data;
          console.log(this.delmes);
        },
        error => {
          if (error.status > 300) {
            return Observable.throw(new Error(error.status));
          }
        }
      );
      alert('Η εγγραφή διεγράφη επιτυχώς');
      this.data.getShops(this.getsort, this.getstatus).subscribe(data => {
          this.shops = data;
          console.log(this.shops);
        }
      );
    }
  }

  onSubmit() {
    this.submitted = true;
    this.data.getShops('id|ASC', 'ALL' ).subscribe(data => {
        this.shops = data;
        console.log(this.shops);
      }
    );
  }

}

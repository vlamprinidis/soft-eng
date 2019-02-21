import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }


  firstClick() {
    // this.router.navigateByUrl('/about');
    return console.log('clicked');
  }

  getProducts(sort, status ) {
    let params = new HttpParams().set('sort', sort).set('status', status);
    return this.http.get('http://localhost:8765/observatory/api/products', { params: params});
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})

export class DataService {

  constructor(private http: HttpClient) {
  }


  firstClick() {
    // this.router.navigateByUrl('/about');
    return console.log('clicked');
  }

  getProducts(sort, status) {
    const params = new HttpParams().set('sort', sort).set('status', status);
    return this.http.get('http://localhost:8765/observatory/api/products', {params: params});
  }

  getProduct(id) {
    return this.http.get('http://localhost:8765/observatory/api/products/' + id);
  }

  addProduct(name, description, category, tags) {
    console.log(name);
    console.log(description);
    console.log(category);
    console.log(tags);
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      .set('X-OBSERVATORY-AUTH', 'cm9vdGtva286cm9vdGtva28=');
    // const product = [name, description, category, tags];
    return this.http.post('http://localhost:8765/observatory/api/products',
      {name: name, description: description, category: category, tags: tags}, {headers: headers});
  }
}

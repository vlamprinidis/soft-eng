import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {JwtService} from './jwt.service';

@Injectable({
  providedIn: 'root'
})

export class DataService {

  constructor(private http: HttpClient, private jwt: JwtService) {
  }

  getProducts(sort, status) {
    let params = new HttpParams();
    params = params.set('sort', sort).set('status', status);
    params = params.set('count', '100');
    return this.http.get('https://localhost:8765/observatory/api/products', {params: params});
  }

  getProduct(id) {
    return this.http.get('https://localhost:8765/observatory/api/products/' + id);
  }

  addProduct(name, description, category, tags) {
    console.log(name);
    console.log(description);
    console.log(category);
    console.log(tags);
    const body = `name=${name}&description=${description}&category=${category}&tags=${tags}`;
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded; charset=utf-8')
      .set('X-OBSERVATORY-AUTH', this.jwt.giveToken());
    return this.http.post('https://localhost:8765/observatory/api/products',
     body, {headers: headers});
  }

  updateProduct(name, description, category, tags, id) {
    console.log(name);
    console.log(description);
    console.log(category);
    console.log(tags);
    // token = blah blah;
    const body = `name=${name}&description=${description}&category=${category}&tags=${tags}`;
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      .set('X-OBSERVATORY-AUTH', this.jwt.giveToken());
    return this.http.put('https://localhost:8765/observatory/api/products/' + id,
      body, {headers: headers});
  }

  deleteProduct(id) {
    console.log(id);
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      .set('X-OBSERVATORY-AUTH', this.jwt.giveToken());
    return this.http.delete('https://localhost:8765/observatory/api/products/' + id, {headers: headers});
  }

// SHOP TIME

  getShops(sort, status) {
    let params = new HttpParams();
    params = params.set('sort', sort).set('status', status);
    params = params.set('count', '100');
    return this.http.get('https://localhost:8765/observatory/api/shops', {params: params});
  }

  getShop(id) {
    return this.http.get('https://localhost:8765/observatory/api/shops/' + id);
  }

  addShop(name, address, lng, lat, tags) {
    console.log(name);
    console.log(address);
    console.log(lng);
    console.log(lat);
    console.log(tags);
    // token = blah blah;
    const body = `name=${name}&address=${address}&lng=${lng}&lat=${lat}&tags=${tags}`;
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      .set('X-OBSERVATORY-AUTH', this.jwt.giveToken());
    return this.http.post('https://localhost:8765/observatory/api/shops',
      body, {headers: headers});
  }

  updateShop(name, address, lng, lat, tags, id) {
    console.log(name);
    console.log(address);
    console.log(lng);
    console.log(lat);
    console.log(tags);
    // token = blah blah;
    const body = `name=${name}&address=${address}&lng=${lng}&lat=${lat}&tags=${tags}`;
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      .set('X-OBSERVATORY-AUTH', this.jwt.giveToken());
    return this.http.put('https://localhost:8765/observatory/api/shops/' + id,
      body, {headers: headers});
  }

  deleteShop(id) {
    console.log(id);
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      .set('X-OBSERVATORY-AUTH', this.jwt.giveToken());
    return this.http.delete('https://localhost:8765/observatory/api/shops/' + id, {headers: headers});
  }

  // PRICE TIME
  getPrices(sort, geoDist, geoLng, geoLat, dateFrom, dateTo, products, shops, tags) {
    console.log('coords are' + geoLng + geoLat);
    let params = new HttpParams();
    params = params.set('count', '500');
    // params = params.set('sort', sort);
    if (sort !== '') {
      const str_array = sort.split(',');
      for (let i = 0; i < str_array.length; i++) {
        if ( i === 0) {
          params = params.set('sort', str_array[i]);
        } else {
          params = params.append('sort', str_array[i]);
        }
      }
    }
    if (geoDist !== '' && geoLng !== '' && geoLat !== '') {
      params = params.set('geoDist', geoDist);
      params = params.set('geoLng', geoLng);
      params = params.set('geoLat', geoLat);
    }
    if (dateFrom !== '' && dateTo !== '') {
      params = params.set('dateFrom', dateFrom);
      params = params.set('dateTo', dateTo);
    }
    if (products !== '') {
      const str_array = products.split(',');
      for (let i = 0; i < str_array.length; i++) {
        if ( i === 0) {
          params = params.set('products', str_array[i]);
        } else {
          params = params.append('products', str_array[i]);
        }
      }
    }
    if (shops !== '') {
      const str_array = shops.split(',');
      for (let i = 0; i < str_array.length; i++) {
        if ( i === 0) {
          params = params.set('shops', str_array[i]);
        } else {
          params = params.append('shops', str_array[i]);
        }
      }
    }
    // console.log(tags);
    if (tags !== '') {
      const str_array = tags.split(',');
      for (let i = 0; i < str_array.length; i++) {
        str_array[i] = str_array[i].replace(/^\s*/, '').replace(/\s*$/, '');
        if ( i === 0) {
          params = params.set('tags', str_array[i]);
        } else {
          params = params.append('tags', str_array[i]);
        }
        // console.log(str_array[i]);
      }
    }
    console.log(params);
    return this.http.get('https://localhost:8765/observatory/api/prices', {params: params});
  }

  addPrice(price, dateTo, productId, shopId) {
    console.log(price);
    console.log(dateTo);
    console.log(productId);
    console.log(shopId);
    // token = blah blah;
    const body = `price=${price}&dateTo=${dateTo}&productId=${productId}&shopId=${shopId}`;
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
      .set('X-OBSERVATORY-AUTH', this.jwt.giveToken());
    return this.http.post('https://localhost:8765/observatory/api/prices',
      body, {headers: headers});
  }


  // REGISTER
  addUser(username, password, name, email, admin) {
    console.log(username);
    console.log(password);
    console.log(name);
    console.log(email);
    console.log(admin);
    const token = '';
    const body = `username=${username}&password=${password}&name=${name}&email=${email}&admin=${admin}&token=${token}`;
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded; charset=utf-8')
      .set('X-OBSERVATORY-AUTH', 'cm9vdGtva286cm9vdGtva28=');
    return this.http.post('https://localhost:8765/observatory/api/signup',
      body, {headers: headers});
  }

  getUser() {
    return this.http.get('https://localhost:8765/observatory/api/signup/');
  }
}

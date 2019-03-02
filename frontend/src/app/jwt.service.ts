import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {tap} from 'rxjs/operators';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class JwtService {
  constructor(private httpClient: HttpClient) { }

  login(username: string, password: string) {
    const body = `username=${username}&password=${password}`;
    const headers = new HttpHeaders({
        'Content-Type' : 'application/x-www-form-urlencoded'
      });
    return this.httpClient.post<{token: string}>('https://localhost:8765/observatory/api/login',
      body, {headers: headers}).pipe(
        tap(result => { localStorage.setItem('token', result.token);
        localStorage.setItem('username', username); } )
    );
  }

  logout(token: string) {
    const httpHeaders = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded; charset=utf-8')
      .set('X-OBSERVATORY-AUTH', token);
    const options = {
      headers: httpHeaders
    };
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    return this.httpClient.post<{message: string}>('https://localhost:8765/observatory/api/logout', token, options).subscribe(
      data => console.log('success', data.message),
      error => console.log('oops', error)
    );

  }

  public LoggedIn(): boolean {
    return localStorage.getItem('token') !==  null;
  }

  public giveToken() {
    return localStorage.getItem('token');
  }

  public giveName() {
    return localStorage.getItem('username');
  }
}

import { Injectable } from '@angular/core';
import {JwtService} from './jwt.service';
import { Router, CanActivate } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(public auth: JwtService, public router: Router) {}

  canActivate(): boolean {
    if (!this.auth.LoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}

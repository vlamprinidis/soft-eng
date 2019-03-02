import { Component, OnInit } from '@angular/core';
import { JwtService} from '../jwt.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {
  appTitle = 'Click \'n\' Park';
  log: string;
  token: string;

  constructor(private jwt: JwtService) { }

  ngOnInit() {
    setInterval(() => {
    if (!this.jwt.LoggedIn()) { this.log = 'Σύνδεση'; } else { this.log = 'Αποσύνδεση'; }
    }, 250 );
  }

}

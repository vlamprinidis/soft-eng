import { Component, OnInit } from '@angular/core';
import { JwtService} from '../jwt.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  logged = false;
  username: string;

  constructor(private jwt: JwtService) { }

  ngOnInit() {
    if ( this.jwt.LoggedIn() ) {
      this.logged = true;
      this.username = this.jwt.giveName();
    }
  }

}

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

  constructor(private data: JwtService) { }

  ngOnInit() {
    if ( this.data.LoggedIn() ) {
      this.logged = true;
      this.username = this.data.giveName();
    }
  }

}

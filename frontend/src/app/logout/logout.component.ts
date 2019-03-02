import { Component, OnInit } from '@angular/core';
import { JwtService} from '../jwt.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {
  token: string;

  constructor(private jwt: JwtService,  public router: Router) { }

  ngOnInit() {
  }

  onClickMe() {
    if (!this.jwt.LoggedIn()) { this.router.navigate(['/login']); return; }
    this.token = this.jwt.giveToken();
    this.jwt.logout(this.token);
    this.router.navigate(['/login']);
    return;
  }

}

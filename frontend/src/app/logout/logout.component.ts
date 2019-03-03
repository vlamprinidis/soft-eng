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
  username: string;

  constructor(private jwt: JwtService,  public router: Router) { }

  ngOnInit() {
    if(this.jwt.LoggedIn()){
      this.username = this.jwt.giveName();
    }
  }

  onClickMe() {
    if (!this.jwt.LoggedIn()) { this.router.navigate(['/login']); return; }
    this.token = this.jwt.giveToken();
    this.jwt.logout(this.token);
    alert('Αποσυνδέθηκες με επιτυχία!');
    this.router.navigate(['']);
    return;
  }

}

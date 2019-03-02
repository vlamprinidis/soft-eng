import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {JwtService} from '../jwt.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  username: string;
  loginForm: FormGroup;
  submitted = false;
  success = false;

  constructor(private formBuilder: FormBuilder, private data: JwtService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }
    // console.log(this.loginForm.controls.username.value);
    // console.log(this.loginForm.controls.password.value);
    this.username = this.loginForm.controls.username.value;
    this.success = true;
    this.data.login(this.loginForm.controls.username.value, this.loginForm.controls.password.value).subscribe(data => {
      console.log(data['token']);
    });
  }

}

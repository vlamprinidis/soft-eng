import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {NgxPaginationModule} from 'ngx-pagination';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { ShopsComponent } from './shops/shops.component';
import { ShowprodComponent } from './showprod/showprod.component';
import { NewprodComponent } from './newprod/newprod.component';
import { LoginComponent } from './login/login.component';

import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { LoginhomeComponent } from './loginhome/loginhome.component';
import { UpdateprodComponent } from './updateprod/updateprod.component';


@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    AboutComponent,
    ContactComponent,
    HomeComponent,
    ShopsComponent,
    ShowprodComponent,
    NewprodComponent,
    LoginComponent,
    LoginhomeComponent,
    UpdateprodComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxPaginationModule,
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }],
  bootstrap: [AppComponent, HomeComponent]
})
export class AppModule { }

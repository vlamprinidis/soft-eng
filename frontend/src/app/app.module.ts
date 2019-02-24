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

import { LoginComponent } from './login/login.component';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { LoginhomeComponent } from './loginhome/loginhome.component';

import { PricesComponent } from './prices/prices.component';

import { ProductsComponent } from './products/products.component';
import { ProdNewComponent } from './prod-new/prod-new.component';
import { ProdShowComponent } from './prod-show/prod-show.component';
import { ProdUpdateComponent } from './prod-update/prod-update.component';

import { ShopsComponent } from './shops/shops.component';
import { ShopNewComponent } from './shop-new/shop-new.component';
import { ShopShowComponent } from './shop-show/shop-show.component';
import { ShopUpdateComponent } from './shop-update/shop-update.component';


@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    AboutComponent,
    ContactComponent,
    HomeComponent,
    LoginComponent,
    LoginhomeComponent,
    PricesComponent,
    ProductsComponent,
    ProdNewComponent,
    ProdShowComponent,
    ProdUpdateComponent,
    ShopsComponent,
    ShopNewComponent,
    ShopShowComponent,
    ShopUpdateComponent,
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

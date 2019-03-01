import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgxPaginationModule } from 'ngx-pagination';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavComponent } from './nav/nav.component';
import { HomeComponent } from './home/home.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { RegisterComponent } from './register/register.component';

import { ProductsComponent } from './products/products.component';
import { ProdNewComponent } from './prod-new/prod-new.component';
import { ProdShowComponent } from './prod-show/prod-show.component';
import { ProdUpdateComponent } from './prod-update/prod-update.component';

import { ShopsComponent } from './shops/shops.component';
import { ShopNewComponent } from './shop-new/shop-new.component';
import { ShopShowComponent } from './shop-show/shop-show.component';
import { ShopUpdateComponent } from './shop-update/shop-update.component';

import { PricesComponent } from './prices/prices.component';
import { PriceNewComponent } from './price-new/price-new.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTabsModule } from '@angular/material/tabs';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    HomeComponent,
    PricesComponent,
    ProductsComponent,
    ProdNewComponent,
    ProdShowComponent,
    ProdUpdateComponent,
    ShopsComponent,
    ShopNewComponent,
    ShopShowComponent,
    ShopUpdateComponent,
    PriceNewComponent,
    RegisterComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxPaginationModule,
    BrowserAnimationsModule,
    MatTabsModule,
  ],
  providers: [],
  bootstrap: [AppComponent, HomeComponent]
})
export class AppModule { }

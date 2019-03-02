import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';

import { ProductsComponent } from './products/products.component';
import { ProdNewComponent} from './prod-new/prod-new.component';
import { ProdShowComponent} from './prod-show/prod-show.component';
import { ProdUpdateComponent} from './prod-update/prod-update.component';

import { ShopsComponent } from './shops/shops.component';
import { ShopNewComponent } from './shop-new/shop-new.component';
import { ShopShowComponent } from './shop-show/shop-show.component';
import { ShopUpdateComponent } from './shop-update/shop-update.component';
import {PricesComponent} from './prices/prices.component';
import {PriceNewComponent} from './price-new/price-new.component';
import {LoginComponent} from './login/login.component';
import {LogoutComponent} from './logout/logout.component';

import { RegisterComponent } from './register/register.component';

import { AuthGuardService as AuthGuard } from './auth-guard.service';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'products', component: ProductsComponent, canActivate: [AuthGuard] },
  { path: 'prod-show', component: ProdShowComponent, canActivate: [AuthGuard] },
  { path: 'prod-new', component: ProdNewComponent, canActivate: [AuthGuard] },
  { path: 'prod-update', component: ProdUpdateComponent, canActivate: [AuthGuard] },
  { path: 'shops', component: ShopsComponent, canActivate: [AuthGuard] },
  { path: 'shop-new', component: ShopNewComponent, canActivate: [AuthGuard] },
  { path: 'shop-show', component: ShopShowComponent, canActivate: [AuthGuard] },
  { path: 'shop-update', component: ShopUpdateComponent, canActivate: [AuthGuard] },
  { path: 'prices', component: PricesComponent, canActivate: [AuthGuard] },
  { path: 'price-new', component: PriceNewComponent, canActivate: [AuthGuard]},
  { path: 'register', component: RegisterComponent},
  { path: 'login', component: LoginComponent},
  { path: 'logout', component: LogoutComponent, canActivate: [AuthGuard]},
  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

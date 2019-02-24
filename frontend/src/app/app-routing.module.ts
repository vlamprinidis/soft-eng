import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './_guards';
import { LoginhomeComponent } from './loginhome/loginhome.component';

import {ProductsComponent} from './products/products.component';
import { ProdNewComponent} from './prod-new/prod-new.component';
import { ProdShowComponent} from './prod-show/prod-show.component';
import { ProdUpdateComponent} from './prod-update/prod-update.component';
import { ShopsComponent } from './shops/shops.component';
import { ShopNewComponent } from './shop-new/shop-new.component';
import { ShopShowComponent } from './shop-show/shop-show.component';
import { ShopUpdateComponent } from './shop-update/shop-update.component';
import {PricesComponent} from './prices/prices.component';

const routes: Routes = [
  { path: '', component: HomeComponent },//, canActivate: [AuthGuard] },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'products', component: ProductsComponent },
  { path: 'prod-show', component: ProdShowComponent },
  { path: 'prod-new', component: ProdNewComponent },
  { path: 'prod-update', component: ProdUpdateComponent },
  { path: 'shops', component: ShopsComponent },
  { path: 'shop-new', component: ShopNewComponent },
  { path: 'shop-show', component: ShopShowComponent },
  { path: 'shop-update', component: ShopUpdateComponent },
  { path: 'prices', component: PricesComponent },
  { path: 'login', component: LoginComponent },
  { path: 'loginhome', component: LoginhomeComponent},

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { AboutComponent } from './about/about.component';
import { ContactComponent } from './contact/contact.component';
import { ShopsComponent } from './shops/shops.component';
import { ShowprodComponent} from './showprod/showprod.component';
import { NewprodComponent} from './newprod/newprod.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './_guards';
import { LoginhomeComponent } from './loginhome/loginhome.component';
import { UpdateprodComponent} from './updateprod/updateprod.component';

const routes: Routes = [
  { path: '', component: HomeComponent },//, canActivate: [AuthGuard] },
  { path: 'about', component: AboutComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'shops', component: ShopsComponent },
  { path: 'showprod', component: ShowprodComponent },
  { path: 'newprod', component: NewprodComponent },
  { path: 'updateprod', component: UpdateprodComponent },
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

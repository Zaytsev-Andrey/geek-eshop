import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { FooterComponent } from './components/footer/footer.component';
import { ProductGalleryPageComponent } from './pages/product-gallery-page/product-gallery-page.component';
import {HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import { ProductCardComponent } from './pages/product-card/product-card.component';
import { ProductFilterComponent } from './components/product-filter/product-filter.component';
import { ProductGalleryComponent } from './components/product-gallery/product-gallery.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import { CartPageComponent } from './pages/cart-page/cart-page.component';
import { OrderPageComponent } from './pages/order-page/order-page.component';
import { LoginPageComponent } from './pages/login-page/login-page.component';
import {UnauthorizedInterceptor} from "./helpers/unauthorized-interceptor";
import { OrderDetailPageComponent } from './pages/order-detail-page/order-detail-page.component';

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    FooterComponent,
    ProductGalleryPageComponent,
    ProductCardComponent,
    ProductFilterComponent,
    ProductGalleryComponent,
    PaginationComponent,
    CartPageComponent,
    OrderPageComponent,
    LoginPageComponent,
    OrderDetailPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({cookieName: 'XSRF-TOKEN'})
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: UnauthorizedInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

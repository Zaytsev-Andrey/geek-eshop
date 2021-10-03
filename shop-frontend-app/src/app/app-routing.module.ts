import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PRODUCT_GALLERY_PAGE_URL, ProductGalleryPageComponent} from "./pages/product-gallery-page/product-gallery-page.component";
import {PRODUCT_CARD_URL, ProductCardComponent} from "./pages/product-card/product-card.component";
import {CART_PAGE_URL, CartPageComponent} from "./pages/cart-page/cart-page.component";
import {ORDER_URL, OrderPageComponent} from "./pages/order-page/order-page.component";
import {AuthGuard} from "./helpers/auth-guard";
import {LOGIN_PAGE_URL, LoginPageComponent} from "./pages/login-page/login-page.component";
import {ORDER_DETAIL_PAGE_URL, OrderDetailPageComponent} from "./pages/order-detail-page/order-detail-page.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: PRODUCT_GALLERY_PAGE_URL},
  {path: PRODUCT_GALLERY_PAGE_URL, component: ProductGalleryPageComponent},
  {path: CART_PAGE_URL, component: CartPageComponent},
  {path: LOGIN_PAGE_URL, component: LoginPageComponent},
  {path: ORDER_URL, component: OrderPageComponent, canActivate: [AuthGuard]},
  {path: ORDER_DETAIL_PAGE_URL, component: OrderDetailPageComponent},
  {path: PRODUCT_CARD_URL, component: ProductCardComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

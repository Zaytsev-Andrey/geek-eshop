import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PRODUCT_GALLERY_PAGE_URL, ProductGalleryPageComponent} from "./pages/product-gallery-page/product-gallery-page.component";
import {PRODUCT_CARD_URL, ProductCardComponent} from "./pages/product-card/product-card.component";
import {CART_PAGE_URL, CartPageComponent} from "./pages/cart-page/cart-page.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: PRODUCT_GALLERY_PAGE_URL},
  {path: PRODUCT_GALLERY_PAGE_URL, component: ProductGalleryPageComponent},
  {path: CART_PAGE_URL, component: CartPageComponent},
  {path: PRODUCT_CARD_URL, component: ProductCardComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

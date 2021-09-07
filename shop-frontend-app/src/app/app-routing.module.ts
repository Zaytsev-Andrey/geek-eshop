import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PRODUCT_GALLERY_URL, ProductGalleryComponent} from "./pages/product-gallery/product-gallery.component";
import {PRODUCT_CARD_URL, ProductCardComponent} from "./pages/product-card/product-card.component";

const routes: Routes = [
  {path: "", pathMatch: "full", redirectTo: PRODUCT_GALLERY_URL},
  {path: PRODUCT_GALLERY_URL, component: ProductGalleryComponent},
  {path: PRODUCT_CARD_URL, component: ProductCardComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

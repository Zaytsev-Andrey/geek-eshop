import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../model/product";
import {Page} from "../../model/page";
import {ProductFilterDto} from "../../model/product-filter-dto";

export const PRODUCT_GALLERY_PAGE_URL = 'product';

@Component({
  selector: 'app-product-gallery-page',
  templateUrl: './product-gallery-page.component.html',
  styleUrls: ['./product-gallery-page.component.scss']
})
export class ProductGalleryPageComponent implements OnInit {

  products: Product[] = [];

  page?: Page;

  productFilterDto?: ProductFilterDto;

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.productService.findAll()
      .subscribe(res => {
        console.log(`Products is loading`);
        this.page = res;
        this.products = res.content;
      }, err =>{
        console.error(`Products were not loaded`, err);
      });
  }

  filterApplied($event: ProductFilterDto) {
    this.productFilterDto = $event;

    this.productService.findAll($event)
      .subscribe(res => {
        this.page = res;
        this.products = res.content;
      }, err => {
        console.error(`Products were not loaded`, err)
      })
  }

  goToPage($event: number) {
    this.productService.findAll(this.productFilterDto, $event)
      .subscribe(res => {
        this.page = res;
        this.products = res.content;
      }, err => {
        console.error(`Products were not loaded`, err)
      })
  }
}

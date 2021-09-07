import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../model/product";
import {Page} from "../../model/page";

export const PRODUCT_GALLERY_URL = 'product';

@Component({
  selector: 'app-product-gallery',
  templateUrl: './product-gallery.component.html',
  styleUrls: ['./product-gallery.component.scss']
})
export class ProductGalleryComponent implements OnInit {

  products: Product[] = [];
  page: Page = {} as Page;
  pages: number[] = [];
  activePage: number = 1;
  isError: boolean = false;

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.retrieveProducts();
  }

  private retrieveProducts() {
    this.productService.findAll()
      .then(res => {
        this.update(res);
      })
      .catch(err => {
        console.error(err);
        this.isError = true;
      })
  }

  private getArrayOfPage(count: number): number[] {
    const arr = [];
    for (let i = 1; i <= count; i++) {
      arr.push(i);
    }
    return arr;
  }

  private update(res: Page) {
    this.products = res.content;
    this.page = res;
    this.pages = this.getArrayOfPage(res.totalPages);
    // this.totalPages = res.totalPages;
    this.activePage = res.number + 1;
    // this.last = res.last;
    // this.first = res.first;
  }

  onClickPage(pageNumber: number) {
    if (pageNumber > 0 && pageNumber <= this.page.totalPages) {
      this.productService.findOfPage(pageNumber)
        .then(res => {
          this.update(res);
        })
        .catch(err => {
          console.error(err);
          this.isError = true;
        })
    }
  }
}

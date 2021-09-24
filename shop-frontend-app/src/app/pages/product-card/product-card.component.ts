import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../model/product";
import {ActivatedRoute} from "@angular/router";

export const PRODUCT_CARD_URL = 'product/:id';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent implements OnInit {

  product: Product = {} as Product;

  constructor(private productService: ProductService,
              private activateRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.retrieveProduct();
  }

  private retrieveProduct() {
    this.productService.findById(this.activateRoute.snapshot.params['id'])
      .then(res => {
        this.product = res;
      })
      .catch(err => {
        console.error(err);
      })

  }

}

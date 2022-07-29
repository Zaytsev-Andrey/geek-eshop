import { Component, OnInit } from '@angular/core';
import {ProductService} from "../../services/product.service";
import {Product} from "../../model/product";
import {ActivatedRoute} from "@angular/router";
import {LineItemDto} from "../../model/line-item-dto";
import {CartService} from "../../services/cart.service";

export const PRODUCT_CARD_URL = 'product/:id';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent implements OnInit {

  product: Product = {} as Product;

  qty: number = 1;

  constructor(private productService: ProductService,
              private cartService: CartService,
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

  addToCart(id: string) {
    console.error(this.qty);
    this.cartService.addToCart(new LineItemDto(id, false, false, this.qty))
      .subscribe();
  }
}

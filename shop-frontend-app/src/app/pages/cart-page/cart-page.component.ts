import {Component, Input, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {AllCartDto} from "../../model/all-cart-dto";
import {LineItem} from "../../model/line-item";
import {AddLineItemDto} from "../../model/add-line-item-dto";

export const CART_PAGE_URL = 'cart';

@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.scss']
})
export class CartPageComponent implements OnInit {

  content?: AllCartDto;

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
    this.cartService.findAll()
      .subscribe(res => {
        this.content = res;
      }, err => {
        console.error('Cart was not loaded', err);
      })
  }

  updateLineItem(lineItem: LineItem) {
    this.cartService.updateLineItem(new AddLineItemDto(lineItem.productId,
      lineItem.color,
      lineItem.material,
      lineItem.saveGiftWrap,
      lineItem.giftWrap,
      lineItem.qty))
      .subscribe(res => {
        this.content = res;
      });
  }

  removeLineItem(lineItem: LineItem) {
    this.cartService.removeLineItem(new AddLineItemDto(lineItem.productId,
      lineItem.color,
      lineItem.material,
      lineItem.saveGiftWrap,
      lineItem.giftWrap,
      lineItem.qty))
      .subscribe(res => {
        this.content = res;
      })
  }

  clearCart() {
    this.cartService.clearCart()
      .subscribe(res => {
        this.content = res;
      })
  }
}

import {Component, Input, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {AllCartDto} from "../../model/all-cart-dto";
import {LineItem} from "../../model/line-item";
import {LineItemDto} from "../../model/line-item-dto";
import {OrderService} from "../../services/order.service";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";

export const CART_PAGE_URL = 'cart';

@Component({
  selector: 'app-cart-page',
  templateUrl: './cart-page.component.html',
  styleUrls: ['./cart-page.component.scss']
})
export class CartPageComponent implements OnInit {

  content?: AllCartDto;

  constructor(private cartService: CartService,
              private orderService: OrderService,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    this.cartService.findAll()
      .subscribe(res => {
        this.content = res;
      }, err => {
        console.error('Cart was not loaded', err);
      })
  }

  updateLineItem(lineItem: LineItem) {
    this.cartService.updateLineItem(new LineItemDto(lineItem.id,
      lineItem.changeGiftWrap,
      lineItem.giftWrap,
      lineItem.qty))
      .subscribe(res => {
        this.content = res;
      });
  }

  removeLineItem(lineItem: LineItem) {
    this.cartService.removeLineItem(new LineItemDto(lineItem.id,
      lineItem.changeGiftWrap,
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

  createOrder() {
    if (this.authService.isAuthenticated()) {
      this.orderService.createOrder().subscribe(res => {
        this.content = res;
      }, err => {
        console.error('Creating order error', err);
      })
    } else {
      this.router.navigateByUrl("/login");
    }
  }
}

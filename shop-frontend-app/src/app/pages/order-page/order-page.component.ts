import { Component, OnInit } from '@angular/core';
import {Order} from "../../model/order";
import {OrderService} from "../../services/order.service";

export const ORDER_URL = 'order';

@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.scss']
})
export class OrderPageComponent implements OnInit {

  orders: Order[] = [];

  constructor(public orderService: OrderService) { }

  ngOnInit(): void {
    this.orderService.findOrdersByUser()
      .then(res => {
        this.orders = res;
      }).catch(err => {
        console.log('Orders were not load', err);
    })
  }

  removeOrder(id: number) {
    this.orderService.removeOrder(id)
      .then(res => {
        this.orders = res;
      }).catch(err => {
      console.log('Orders were not load', err);
    })
  }
}

import { Component, OnInit } from '@angular/core';
import {Order} from "../../model/order";
import {OrderService} from "../../services/order.service";
import {OrderStatusService} from "../../services/order-status.service";

export const ORDER_URL = 'order';

@Component({
  selector: 'app-order-page',
  templateUrl: './order-page.component.html',
  styleUrls: ['./order-page.component.scss']
})
export class OrderPageComponent implements OnInit {

  orders: Order[] = [];

  constructor(public orderService: OrderService,
              public orderStatusService: OrderStatusService) { }

  ngOnInit(): void {
    this.orderService.findOrdersByUser()
      .then(res => {
        this.orders = res;
      }).catch(err => {
        console.log('Orders were not load', err);
    })
    this.orderStatusService.onMessage('/order_out/order')
      .subscribe(msg => {
        this.changeStatus(msg);
      });
  }

  changeStatus(order: Order) {
    console.log(`MESSAGE: order with id='${order.id}' and status='${order.status}'`);
    // this.orders.forEach(o => {
    //   if (o.id == order.id) {
    //     o.status = order.status;
    //   }
    // })
    let changeOrder = this.orders.find(o => o.id === order.id);
    if (changeOrder) {
      changeOrder.status = order.status;
    }
  }

  removeOrder(id: string) {
    this.orderService.removeOrder(id)
      .then(res => {
        this.orders = res;
      }).catch(err => {
      console.log('Orders were not load', err);
    })
  }
}

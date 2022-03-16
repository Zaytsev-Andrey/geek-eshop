import { Component, OnInit } from '@angular/core';
import {OrderDetail} from "../../model/orderDetail";
import {ActivatedRoute} from "@angular/router";
import {OrderService} from "../../services/order.service";

export const ORDER_DETAIL_PAGE_URL = 'order/:id'

@Component({
  selector: 'app-order-detail-page',
  templateUrl: './order-detail-page.component.html',
  styleUrls: ['./order-detail-page.component.scss']
})
export class OrderDetailPageComponent implements OnInit {

  orderDetails: OrderDetail[] = [];

  constructor(private orderService: OrderService,
              private activateRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.orderService.findOrderById(this.activateRoute.snapshot.params['id'])
      .then(res => {
        this.orderDetails = res;
      }).catch(err => {
        console.error('OrderDetails were not load', err);
    })
  }

  editOrderDetail(detail: OrderDetail) {
    this.orderService.editOrderDetail(detail)
      .then(res => {
        this.orderDetails = res;
      }).catch(err => {
      console.error('OrderDetails were not load', err);
    })
  }

  removeOrderDetail(id: string) {
    this.orderService.removeOrderDetail(id)
      .then(res => {
        this.orderDetails = res;
      }).catch(err => {
      console.error('OrderDetails were not load', err);
    })
  }
}

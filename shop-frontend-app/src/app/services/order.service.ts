import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Order} from "../model/order";
import {Observable} from "rxjs";
import {AllCartDto} from "../model/all-cart-dto";
import {OrderDetail} from "../model/orderDetail";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(public http: HttpClient) { }

  public findOrdersByUser() {
    return this.http.get<Order[]>(`/api/v1/order/own`).toPromise();
  }

  public findOrderById(id: number) {
    return this.http.get<OrderDetail[]>(`/api/v1/order/${id}`).toPromise();
  }

  public createOrder() {
    return this.http.post(`/api/v1/order`, {});
  }

  public editOrderDetail(orderDetail: OrderDetail) {
    return this.http.put<OrderDetail[]>(`/api/v1/order`, orderDetail).toPromise();
  }

  public removeOrder(id: string) {
    return this.http.delete<Order[]>(`/api/v1/order/${id}`).toPromise();
  }

  public removeOrderDetail(id: string) {
    return this.http.delete<OrderDetail[]>(`/api/v1/order/detail/${id}`).toPromise();
  }
}

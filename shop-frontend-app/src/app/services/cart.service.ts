import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {LineItemDto} from "../model/line-item-dto";
import {Observable} from "rxjs";
import {AllCartDto} from "../model/all-cart-dto";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient) { }

  public findAll(): Observable<AllCartDto> {
    return this.http.get<AllCartDto>(`/api/v1/cart/all`);
  }

  public addToCart(addLineItemDto: LineItemDto) {
    return this.http.post(`/api/v1/cart`, addLineItemDto);
  }

  public updateLineItem(addLineItemDto: LineItemDto): Observable<AllCartDto> {
    return this.http.put<AllCartDto>(`/api/v1/cart/`, addLineItemDto);
  }

  public removeLineItem(addLineItemDto: LineItemDto) {
    return this.http.delete<AllCartDto>(`/api/v1/cart`, ({
      body: addLineItemDto
    }));
  }

  public clearCart(): Observable<AllCartDto> {
    return this.http.delete<AllCartDto>(`/api/v1/cart/all`);
  }

  public getSubTotal() {

  }
}

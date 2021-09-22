import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AddLineItemDto} from "../model/add-line-item-dto";
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

  public addToCart(addLineItemDto: AddLineItemDto) {
    return this.http.post(`/api/v1/cart`, addLineItemDto);
  }

  public updateLineItem(addLineItemDto: AddLineItemDto): Observable<AllCartDto> {
    return this.http.post<AllCartDto>(`/api/v1/cart/update`, addLineItemDto);
  }

  public removeLineItem(addLineItemDto: AddLineItemDto): Observable<AllCartDto> {
    return this.http.post<AllCartDto>(`/api/v1/cart/remove`, addLineItemDto);
  }

  public clearCart(): Observable<AllCartDto> {
    return this.http.delete<AllCartDto>(`/api/v1/cart/`);
  }

  public getSubTotal() {

  }
}

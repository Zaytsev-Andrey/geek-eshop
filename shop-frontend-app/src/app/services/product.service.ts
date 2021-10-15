import { Injectable } from '@angular/core';
import {Product} from "../model/product";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Page} from "../model/page";
import {ProductFilterDto} from "../model/product-filter-dto";
import {Observable} from "rxjs";

const PAGE_SIZE: number = 6;

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private products: Product[] = [];

  constructor(private http: HttpClient) { }

  public findAll(productFilterDto?: ProductFilterDto, pageNumber?: number): Observable<Page> {
    let params = new HttpParams();

    if (productFilterDto?.categoriesFilter != null && productFilterDto.categoriesFilter.length > 0) {
      params = params.set("categoriesFilter", productFilterDto.categoriesFilter.toString());
    }
    if (productFilterDto?.titleFilter != null) {
      params = params.set("titleFilter", productFilterDto.titleFilter);
    }
    if (productFilterDto?.minCostFilter != null) {
      params = params.set("minCostFilter", productFilterDto.minCostFilter);
    }
    if (productFilterDto?.maxCostFilter != null) {
      params = params.set("maxCostFilter", productFilterDto.maxCostFilter);
    }

    params = params.set("page", pageNumber != null ? pageNumber : 1);
    params = params.set("size", PAGE_SIZE);

    return this.http.get<Page>(`/api/v1/product/all`, {params: params});
  }

  public findById(id: number) {
    return this.http.get<Product>(`/api/v1/product/${id}`).toPromise();
  }
}

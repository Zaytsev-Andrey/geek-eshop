import { Injectable } from '@angular/core';
import {Category} from "../model/category";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(public http: HttpClient) { }

  public findAll() {
    return this.http.get<Category[]>(`/api/v1/category/all`).toPromise();
  }
}

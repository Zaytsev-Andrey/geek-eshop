import {Category} from "./category";
import {Brand} from "./brand";

export class Product {

  constructor(public id: number,
              public title: string,
              public cost: number,
              public description: string,
              public category: Category,
              public brand: Brand,
              public pictures: number[]) {
  }
}

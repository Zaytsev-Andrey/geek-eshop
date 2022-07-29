import {Category} from "./category";
import {Brand} from "./brand";

export class Product {

  constructor(public id: string,
              public title: string,
              public cost: string,
              public description: string,
              public category: Category,
              public brand: Brand,
              public pictures: string[]) {
  }
}

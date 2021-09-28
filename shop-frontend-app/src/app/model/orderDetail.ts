import {Product} from "./product";

export class OrderDetail {

  constructor(public id: number,
              public productDto: Product,
              public qty: number,
              public cost: number,
              public giftWrap: boolean) {
  }
}

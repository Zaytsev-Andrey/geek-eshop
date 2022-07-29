import {Product} from "./product";

export class OrderDetail {

  constructor(public id: string,
              public productDto: Product,
              public qty: number,
              public cost: string,
              public giftWrap: boolean) {
  }
}

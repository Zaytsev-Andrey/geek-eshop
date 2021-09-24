import {Product} from "./product";

export class LineItem {

  constructor(public productId: number,
              public productDto: Product,
              public color: string,
              public material: string,
              public saveGiftWrap: boolean,
              public giftWrap: boolean,
              public qty: number,
              public itemTotal: number) {
  }
}

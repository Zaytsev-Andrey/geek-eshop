import {Product} from "./product";

export class LineItem {

  constructor(public id: string,
              public productTitle: string,
              public changeGiftWrap: boolean,
              public giftWrap: boolean,
			  public cost: string,
			  public totalCost: string,
              public qty: number,
              public itemTotal: number) {
  }
}

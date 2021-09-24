import {LineItem} from "./line-item";

export class AllCartDto {

  constructor(public lineItems: LineItem[],
              public subtotal: number) {
  }
}

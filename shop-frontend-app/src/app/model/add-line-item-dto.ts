export class AddLineItemDto {


  constructor(public productId: number,
              public color: string,
              public material: string,
              public saveGiftWrap: boolean,
              public giftWrap: boolean,
              public qty: number) {
  }
}

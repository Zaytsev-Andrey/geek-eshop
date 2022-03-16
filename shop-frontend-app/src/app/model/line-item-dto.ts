export class LineItemDto {


  constructor(public id: string,
              public changeGiftWrap: boolean,
              public giftWrap: boolean,
              public qty: number) {
  }
}

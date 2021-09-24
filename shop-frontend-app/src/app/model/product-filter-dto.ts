export class ProductFilterDto {

  constructor(public titleFilter: string,
              public minCostFilter: number,
              public maxCostFilter: number,
              public categoriesFilter: number[] = []) {
  }
}

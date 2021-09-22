import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ProductFilterDto} from "../../model/product-filter-dto";
import {CategoryService} from "../../services/category.service";
import {Category} from "../../model/category";

@Component({
  selector: 'app-product-filter',
  templateUrl: './product-filter.component.html',
  styleUrls: ['./product-filter.component.scss']
})
export class ProductFilterComponent implements OnInit {

  @Output() filterApplied = new EventEmitter<ProductFilterDto>();

  productFilterDto: ProductFilterDto = {} as ProductFilterDto;

  categories: Category[] = [];

  constructor(public categoryService: CategoryService) { }

  ngOnInit(): void {
    this.categoryService.findAll()
      .then(res => {
        this.categories = res;
      }).catch(err => {
        console.error('Categories were not loaded', err);
    })
  }

  applyFilter() {
    this.filterApplied.emit(this.productFilterDto);
  }

}

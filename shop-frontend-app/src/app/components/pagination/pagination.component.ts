import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {Page} from "../../model/page";

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit, OnChanges {

  @Input() page?: Page;

  numbers: number[] = [];

  pageNumber: number = 1;

  @Output() goToPageEvent = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {

  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.page != null) {
      this.numbers = Array.from(Array(this.page.totalPages).keys());
      this.pageNumber = this.page.number + 1;
    }
  }

  goToPage(page: number) {
    console.log(`Page number ${page}`);
    this.goToPageEvent.emit(page);
  }

}

import {Component, Input, OnInit} from '@angular/core';
import {Product} from "../../model/product";
import {CartService} from "../../services/cart.service";
import {LineItemDto} from "../../model/line-item-dto";

@Component({
  selector: 'app-product-gallery',
  templateUrl: './product-gallery.component.html',
  styleUrls: ['./product-gallery.component.scss']
})
export class ProductGalleryComponent implements OnInit {

  @Input() products: Product[] = [];

  constructor(private cartService: CartService) { }

  ngOnInit(): void {
  }

  addToCart(id: string) {
    this.cartService.addToCart(new LineItemDto(id, false, false, 1))
      .subscribe();
  }
}

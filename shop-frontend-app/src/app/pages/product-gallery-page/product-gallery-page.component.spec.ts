import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductGalleryPageComponent } from './product-gallery-page.component';

describe('ProductGalleryComponent', () => {
  let component: ProductGalleryPageComponent;
  let fixture: ComponentFixture<ProductGalleryPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductGalleryPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductGalleryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

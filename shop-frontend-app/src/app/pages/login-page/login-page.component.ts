import { Component, OnInit } from '@angular/core';
import {Credentials} from "../../model/credentials";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {PRODUCT_GALLERY_PAGE_URL} from "../product-gallery-page/product-gallery-page.component";

export const LOGIN_PAGE_URL = 'login';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {

  credentials: Credentials = new Credentials("", "");

  isError: boolean = false;

  constructor(private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
  }

  login() {
    this.authService.authenticated(this.credentials)
      .subscribe(authResult => {
        if (authResult.redirectUrl) {
          this.router.navigateByUrl(authResult.redirectUrl);
        } else {
          this.router.navigateByUrl('/' + PRODUCT_GALLERY_PAGE_URL);
        }
      }, err => {
        this.isError = true;
      })
  }
}

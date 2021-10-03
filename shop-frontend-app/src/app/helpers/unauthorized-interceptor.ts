import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {Router} from "@angular/router";
import {AuthService} from "../services/auth.service";
import {catchError} from "rxjs/operators";
import {Injectable} from "@angular/core";

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(private router: Router,
              private auth: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // console.error('Interceptor unauthorized error');
    if (req.url === '/api/v1/login' || req.url === '/api/v1/logout') {
      next.handle(req);
    }

    return next.handle(req).pipe(catchError(err => {
      if (err.status === 401) {
        console.error('Interceptor unauthorized error');
        this.auth.logout();
      }
      return throwError(err);
    }))
  }

}

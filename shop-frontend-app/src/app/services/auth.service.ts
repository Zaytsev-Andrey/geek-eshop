import { Injectable } from '@angular/core';
import {Credentials} from "../model/credentials";
import {Observable} from "rxjs";
import {AuthResult} from "../model/auth-result";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private currentUser?: Credentials;

  public redirectUrl?: string;

  constructor(private http: HttpClient) {
    let data = localStorage.getItem('current_user');
    if (data) {
      this.currentUser = JSON.parse(data);
    }
  }

  public authenticated(credentials: Credentials): Observable<AuthResult> {
    const header = new HttpHeaders(credentials ? {
      Authorization: 'Basic ' + btoa(credentials.username + ':' + credentials.password)
    } : {});

    return this.http.get('/api/v1/login', {headers: header})
      .pipe(
        map(resp => {
          if ('username' in resp) {
            this.currentUser = resp as Credentials;
            localStorage.setItem('current_user', JSON.stringify(resp));
            return new AuthResult(this.currentUser, this.redirectUrl);
          }
          throw new Error('Authenticated error');
        })
      )
  }

  public logout() {
    if (this.isAuthenticated()) {
      this.currentUser = undefined;
      localStorage.removeItem('current_user');
      this.http.post('/api/v1/logout', {}).subscribe();
    }
  }

  public isAuthenticated(): boolean {
    return !!this.currentUser;
  }
}

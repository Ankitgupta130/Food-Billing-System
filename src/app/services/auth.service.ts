import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest, LoginResponse, StatusResponse } from '../models/login.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseurl = 'http://localhost:8080/api/auth';

  private readonly captchaToken='http://localhost:8080/api/verify-captcha';

  constructor(private readonly http:HttpClient) { }

  login(data: LoginRequest) : Observable<LoginResponse> {
    return this.http.post<LoginResponse> (`${this.baseurl}/login`,data)
  }

  logout() {
    localStorage.removeItem('user');
  }

  saveUserData(user: LoginResponse) {
    localStorage.setItem('user',JSON.stringify(user));
  }

  getUserData(): LoginResponse | null {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  token: string | null = null;
  siteKey = '6Lcm-aQrAAAAAAa5727swjlqcj4aPUjMOJXPLPTh';
 
  reCaptcha(token: string): Observable<any> {
  return this.http.post(
    `${this.captchaToken}`,
    { token: token }, 
    { headers: { 'Content-Type': 'application/json' } }
  );
}

setUserStatus(req: StatusResponse): Observable<any> {
  return this.http.post(`${this.baseurl}/action`,req);
}

 
 
}

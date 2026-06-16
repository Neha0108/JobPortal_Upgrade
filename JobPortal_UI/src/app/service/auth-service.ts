import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegisterRequest } from '../interface/register-request';
import { LoginRequest } from '../interface/login-request';
import { AuthResponse } from '../interface/auth-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor( private http: HttpClient ) {}

  register(data: RegisterRequest) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`,data);
  }

  login(data: LoginRequest) {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, data
    );
  }

  refreshToken(token: string) {
    return this.http.post<AuthResponse>(
      `${this.baseUrl}/refresh`,
      {
        refreshToken: token
      }
    );
  }

}

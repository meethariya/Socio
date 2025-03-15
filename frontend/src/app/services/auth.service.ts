import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  baseUrl = 'http://localhost:8004';
  http = inject(HttpClient);

  constructor() { }

  login(formData: FormData) {
    return this.http.post(this.baseUrl+"/auth/generate-token", formData, {
      responseType: 'text'
    });
  }

  setToken(token: string, rememberMe: boolean) {
    sessionStorage.setItem('token', token);
    if(rememberMe) localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  clearTokens(){
    sessionStorage.removeItem("token");
    localStorage.removeItem("token");
  }

}

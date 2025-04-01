import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { catchError, mergeMap, Observable, of, tap, throwError } from 'rxjs';
import { ExceptionResponse } from '../models/exception-response.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'http://localhost:8004';
  private http = inject(HttpClient);
  userProfile: User | null = null;

  login(formData: FormData) {
    return this.http.post(this.baseUrl + '/auth/generate-token', formData, {
      responseType: 'text',
    });
  }

  register(formData: FormData) {
    return this.http.post<User>(this.baseUrl + '/user', formData);
  }

  setToken(token: string, rememberMe: boolean) {
    sessionStorage.setItem('token', token);
    if (rememberMe) localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  clearTokens() {
    this.userProfile=null;
    sessionStorage.removeItem('token');
    localStorage.removeItem('token');
  }

  getUser(username: String) {
    return this.http.get<User>(this.baseUrl + `/user/by-username/${username}`);
  }

  setUserProfile(user: User) {
    this.userProfile = user;
  }

  getUserProfile(): Observable<User> {
    if (this.userProfile != null) return of(this.userProfile);

    const token = this.getToken();
    if (token == null) {
      const errorResponse: ExceptionResponse = {
        timestamp: new Date(),
        status: 401,
        error: 'Unauthorized',
        detail: 'No token found',
        isFrontend: true
      };
      return throwError(() => errorResponse);
    }

    const params = new HttpParams().set('token', token);
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http
      .get<{ authId: number }>(`${this.baseUrl}/auth/validate-token`, {params,})
      .pipe(
        mergeMap(({ authId }) =>
          this.http.get<User>(`${this.baseUrl}/user/auth-id/${authId}`, {headers})
        ),
        tap((user) => (this.userProfile = user)),
        catchError((err) => {
          console.error('Error fetching user profile:', err);
          return throwError(() => err);
        })
      );
  }

  public get url() : string {
    return this.baseUrl;
  }

}

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { catchError, mergeMap, Observable, of, tap, throwError } from 'rxjs';

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
    sessionStorage.removeItem('token');
    localStorage.removeItem('token');
  }

  getUser(username: String, token: String) {
    return this.http.get<User>(this.baseUrl + `/user/${username}`, {
      headers: new HttpHeaders({ Authorization: 'Bearer ' + token }),
    });
  }

  setUserProfile(user: User) {
    this.userProfile = user;
  }

  getUserProfile(): Observable<User> {
    if (this.userProfile != null) return of(this.userProfile);

    const token = this.getToken();
    if (token == null) return throwError(() => new Error('No token found'));

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

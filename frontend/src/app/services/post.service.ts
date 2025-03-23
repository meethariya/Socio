import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { throwError } from 'rxjs';
import { Post } from '../models/post.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private baseUrl = 'http://localhost:8004';
  private http = inject(HttpClient);
  private router = inject(Router);

  createPost(formData: FormData) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.post<Post>(this.baseUrl+"/post",formData,{headers:headers});
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}

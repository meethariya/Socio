import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { throwError } from 'rxjs';
import { Post } from '../models/post.model';
import { Router } from '@angular/router';
import { Comment } from '../models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private readonly baseUrl = 'http://localhost:8004';
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

  deletePost(id: string) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.delete<null>(this.baseUrl+`/post/${id}`,{headers:headers});
  }

  getPostById(id: string) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.get<Post>(this.baseUrl+`/post/${id}`,{headers:headers});
  }

  updatePost(id: string, formData: FormData) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.put<Post>(this.baseUrl+`/post/${id}`,formData,{headers:headers});
  }

  likeUnlikePost(formData: FormData, like:boolean) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    if(like) {
      return this.http.post<null>(this.baseUrl+"/like",formData,{headers:headers});
    } else {
      return this.http.delete<null>(this.baseUrl+"/like",{headers:headers, body:formData});
    }
  }

  getCommentsOfPost(postId: string) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.get<Array<Comment>>(this.baseUrl + "/comment/by-post/" + postId, { headers: headers });
  }

  addComment(formData: FormData) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.post<Comment>(this.baseUrl+"/comment", formData, { headers: headers });
  }

  updateComment(formData: FormData, commentId: string) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.put<Comment>(this.baseUrl+"/comment/"+commentId, formData, { headers: headers });
  }

  deleteComment(commentId: string) {
    const token = this.getToken();
    if (token == null){
      this.router.navigate(['/login']);
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.delete<void>(this.baseUrl+"/comment/"+commentId, { headers: headers });
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}

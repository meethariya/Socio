import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Post } from '../models/post.model';
import { User } from '../models/user.model';
import { Friendship } from '../models/friendship.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private readonly baseUrl = environment.baseUrl;
  private http = inject(HttpClient);
  private router = inject(Router);

  getPosts(id: number, visitorId?:number) {
    const headers = this.headerGenerator();
    if (headers == null) return;
    let params = new HttpParams();
    if(visitorId) {
      params = params.set('visitorId', visitorId);
    }
    return this.http.get<Array<Post>>(this.baseUrl + `/post/user/${id}`, {
      headers: headers,
      params: params
    });
  }

  getFriends(id: number) {
    const headers = this.headerGenerator();
    if (headers == null) return;
    return this.http.get<Array<User>>(
      this.baseUrl + `/friend/friends-of-user/${id}`, {
        headers: headers,
      }
    );
  }

  getFriendRequests(id: number) {
    const headers = this.headerGenerator();
    if (headers == null) return;
    return this.http.get<Array<Friendship>>(
      this.baseUrl + `/friend/request-received-by-user/${id}`, {
        headers: headers,
      }
    );
  }

  getProfileSummary(username: string) {
    return this.http.get<{
      id: number,
	    username: string,
	    name: string,
	    friendCount: number,
	    postCount: number
    }>(this.baseUrl+"/user/profile-summary/"+username);
  }

  private headerGenerator(): HttpHeaders | null {
    const token = sessionStorage.getItem('token');
    if (token == null) {
      this.router.navigate(['/login']);
      return null;
    }
    return new HttpHeaders({ Authorization: 'Bearer ' + token });
  }
}

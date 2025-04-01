import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { Friendship, FriendshipStatus } from '../models/friendship.model';
import { User } from '../models/user.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FriendModalComponent } from '../components/friend-modal/friend-modal.component';

@Injectable({
  providedIn: 'root'
})
export class FriendService {
  private baseUrl = 'http://localhost:8004';
  private http = inject(HttpClient);
  modelService = inject(NgbModal);

  openModal(friendList?: Array<User>, requestList?:Array<Friendship>, allowCta?:boolean){
    const userSerachRequired = friendList || requestList ? false: true;
    const modal = this.modelService.open(FriendModalComponent,{
      scrollable: true,
      animation: true,
      backdropClass: "light-blue-backdrop"
    });
    modal.componentInstance.userSerachRequired = userSerachRequired;
    if(friendList) {
      modal.componentInstance.inputFriendList = friendList;
    }
    if(requestList) {
      modal.componentInstance.friendRequests = requestList;
    }
    modal.componentInstance.allowCta = allowCta ?? false;
    return modal;
  }

  searchUser(username: string): Observable<Array<User>> {
    const token = this.getToken();
    if (token == null) return throwError(() => new Error('No token found'));

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const params = new HttpParams().set('query', username);

    return this.http.get<Array<User>>(this.baseUrl + '/user/query-user', {
      params: params,
      headers: headers,
    });
  }

  addFriend(formData:FormData){
    const token = this.getToken();
    if (token == null) return throwError(() => new Error('No token found'));

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.post<Friendship>(this.baseUrl+"/friend",formData,{headers:headers});
  }

  updateFriendship(friendshipId: number, status: FriendshipStatus) {
    const token = this.getToken();
    if (token == null) return throwError(() => new Error('No token found'));

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.put<Friendship>(this.baseUrl+`/friend/${friendshipId}`, status.toString(),{headers:headers});
  }

  deleteFriendship(senderId: number, receiverId:number) {
    const token = this.getToken();
    if (token == null) return throwError(() => new Error('No token found'));

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const params = new HttpParams().set('senderId', senderId).set("receiverId",receiverId);
    return this.http.delete<null>(this.baseUrl+`/friend`, {
      params: params,
      headers: headers
    });
  }

  areFriends(user1:string, user2:string){
    const token = this.getToken();
    if (token == null) return throwError(() => new Error('No token found'));

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const params = new HttpParams().set('user1', user1).set("user2",user2);

    return this.http.get<Map<string, boolean>>(this.baseUrl + '/friend/are-friends', {
      params: params,
      headers: headers,
    });
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}

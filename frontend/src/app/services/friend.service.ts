import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { Friendship } from '../models/friendship.model';
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

  openModal(friendList?: Array<User>){
    const modal = this.modelService.open(FriendModalComponent,{
      scrollable: true,
      animation: true,
      backdropClass: "light-blue-backdrop"
    });
    if(friendList) {
      modal.componentInstance.inputFriendList = friendList;
    }
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

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}

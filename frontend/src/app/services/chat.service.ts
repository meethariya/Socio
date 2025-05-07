import { inject, Injectable } from '@angular/core';
import { Observable, Subject, throwError } from 'rxjs';
import { Client, IMessage, ReconnectionTimeMode } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Message, MessageStatus } from '../models/message.model';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private client: Client;
  private http = inject(HttpClient);
  private connected = false;
  private messageSubject = new Subject<Message>();
  private notificationAudio = new Audio();

  private readonly baseUrl = environment.baseUrl;
  private readonly WS_URL = this.baseUrl+ '/ws';

  constructor() {
    this.client = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      reconnectTimeMode: ReconnectionTimeMode.EXPONENTIAL
    });

    this.client.onConnect = () => {
      console.log('Chat service connected.');
      this.connected = true;
    };
    this.client.activate();
    this.notificationAudio.src = "notification.mp3";
    this.notificationAudio.load();
  }

  public subscribeToMessages(userId: number): void {
    if(this.connected) {
      this.client.subscribe(`/topic/messages.${userId}`, (message: IMessage) => {
        clearTimeout(timeout);
        const msg: Message = JSON.parse(message.body);
        this.messageSubject.next(msg);
      });
    } else {
      var timeout = setTimeout(() => {
        this.subscribeToMessages(userId)
      }, 3000);
    }
  }

  public sendMessage(msg: {
    senderId: number;
    receiverId: number;
    content: string;
    status: MessageStatus;
  }): void {
    this.client.publish({
      destination: '/app/chat.send',
      body: JSON.stringify(msg),
    });
  }

  public getMessages(): Observable<Message> {
    return this.messageSubject.asObservable();
  }

  getChatHistory(userId:number, friendId: number) {
    const token = this.getToken();
    if (token == null){
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const params = new HttpParams().set('userId', userId).set("friendId",friendId);
    return this.http.get<Array<Message>>(this.baseUrl+"/message", {
      headers: headers,
      params: params
    });
  }

  markMessageAsRead(messageId: string) {
    const token = this.getToken();
    if (token == null){
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    return this.http.put<Message>(this.baseUrl+`/message/${messageId}`, {'status': MessageStatus.READ}, {headers: headers});
  }

  playNotification(){
    this.notificationAudio.play().catch(err => console.warn("Failed to play notification: ", err));
  }

  private getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}

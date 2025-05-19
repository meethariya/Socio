import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { BehaviorSubject, Observable, Subject, throwError } from 'rxjs';
import { Client, IMessage, ReconnectionTimeMode } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Message, MessageStatus } from '../models/message.model';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ExceptionResponse } from '../models/exception-response.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private client!: Client;
  private http = inject(HttpClient);
  private messageSubject = new Subject<Message>();
  private onChatPage = new BehaviorSubject<boolean>(false);
  private notificationAudio = new Audio();

  private readonly baseUrl = environment.baseUrl;
  private readonly WS_URL = this.baseUrl+ '/ws';
  private subscribedToMessages = false;

  public userUnreadMessageSenderId: WritableSignal<number[]> = signal([]);

  constructor() {
    if(this.client && this.client.connected) return;

    this.client = new Client({
      webSocketFactory: () => new SockJS(this.WS_URL),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      reconnectTimeMode: ReconnectionTimeMode.EXPONENTIAL
    });

    this.client.onConnect = () => {
      console.log('Chat service connected.');
    };
    this.client.activate();
    this.notificationAudio.src = "notification.mp3";
    this.notificationAudio.load();
  }

  public subscribeToMessages(userId: number): void {
    if(this.client && this.client.connected) {
      if (this.subscribedToMessages) return;
      this.client.subscribe(`/topic/messages.${userId}`, (message: IMessage) => {
        clearTimeout(timeout);
        this.subscribedToMessages = true;
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

  public isOnChatPage(): Observable<boolean> {
    return this.onChatPage.asObservable();
  }

  public setOnChatPage(isOnChatPage: boolean): void {
    this.onChatPage.next(isOnChatPage);
  }

  getChatHistory(userId:number, friendId: number) {
    const token = this.getToken();
    if (token == null){
      const errorResponse: ExceptionResponse = {
        timestamp: new Date(),
        status: 401,
        error: 'Unauthorized',
        detail: 'No token found',
        isFrontend: true
      };
      return throwError(() => errorResponse);
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

  markChatAsRead(userId: number, friendId: number) {
    const token = this.getToken();
    if (token == null){
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    return this.http.put<Message[]>(this.baseUrl+`/message/read-chat`, {'userId': userId, 'friendId': friendId}, {headers: headers});
  }

  playNotification(){
    this.notificationAudio.play().catch(err => console.warn("Failed to play notification: ", err));
  }

  getUserUnreadMessage(userId:number) {
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

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const params = new HttpParams().set('userId', userId);
    return this.http.get<number[]>(`${this.baseUrl}/message/unread-message`, {headers, params: params});
  }

  private getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}

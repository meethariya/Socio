import {
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  Signal,
  ViewChild,
  WritableSignal,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { FooterComponent } from '../../components/footer/footer.component';
import { HeaderComponent } from '../../components/header/header.component';
import { ExceptionResponse } from '../../models/exception-response.model';
import { Message, MessageStatus } from '../../models/message.model';
import { User } from '../../models/user.model';
import { TimeAgoPipe } from '../../pipes/time-ago.pipe';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { ChatService } from '../../services/chat.service';
import { FriendService } from '../../services/friend.service';
import { ProfileService } from '../../services/profile.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-chat',
  imports: [HeaderComponent, FooterComponent, RouterLink, TimeAgoPipe, ReactiveFormsModule],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss',
})
export class ChatComponent implements OnInit, OnDestroy{

  @ViewChild('chatListRef') chatListRef!: ElementRef;

  authService = inject(AuthService);
  alertService = inject(AlertService);
  friendService = inject(FriendService);
  profileService = inject(ProfileService);
  chatService = inject(ChatService);

  userProfile!: Signal<User>;
  userFriends!: WritableSignal<Array<FriendListUser>>;
  currentFriendChat: Signal<User>|undefined;
  messageList: Signal<Array<Message>> = signal([]);

  messageForm = new FormGroup({
    message: new FormControl('', [Validators.required])
  });

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe({
      next: (u) => {
        this.userProfile = signal(u);
        this.chatService.subscribeToMessages(u.id);

        this.chatService.getMessages().subscribe({
          next: (m) => {
            if(m.senderId === this.userProfile().id) {

              // self message
              // remove temp message
              var tempMessage = this.messageList().find(message =>
                message.id === '' &&
                message.senderId === this.userProfile().id &&
                message.content === m.content
              );
              if(tempMessage){
                this.messageList = signal(this.messageList().filter(i => i !== tempMessage));
                this.messageList().push(m);
                this.scrollToBottom();
              }

              // update read status
              this.messageList().forEach(message => {
                if(message.id !== '' && message.id === m.id) message.status = m.status;
              });

            } else if(this.currentFriendChat != undefined && m.senderId === this.currentFriendChat().id) {
              // active chat friend message, send read call and push to message list
              this.chatService.markMessageAsRead(m.id).subscribe({
                next: (message) => m.status = message.status,
                error: (err) => this.errorDisplayer(err)
              });
              this.messageList().push(m);
              this.scrollToBottom();
              this.chatService.playNotification();
            } else {
              // other people chat
              let friendFound = false;
              for (const f of this.userFriends()) {
                if(f.id == m.senderId) {
                  f.hasNewMessage = true;
                  break;
                }
              }
              if(friendFound) {
                this.bumpFriend(m.senderId);
              }
              this.chatService.playNotification();
            }
          }
        });

        this.profileService.getFriends(u.id)?.subscribe({
          next: (friends) => {
            this.userFriends = signal(friends);
          },
          error: (err) => this.errorDisplayer(err),
        });
      },
      error: (err) => this.errorDisplayer(err),
    });
  }

  ngOnDestroy(): void {
    this.currentFriendChat = undefined;
    this.messageList = signal([]);
  }

  mobileChatBack() {
    this.currentFriendChat = undefined;
    this.messageList = signal([]);
  }

  startChat(friend: User) {
    if(this.currentFriendChat != undefined && this.currentFriendChat().id == friend.id) return;
    for (const f of this.userFriends()) {
      if(f.id==friend.id) {
        f.hasNewMessage=false;
        break;
      }
    }
    this.currentFriendChat = signal(friend);
    this.chatService
      .getChatHistory(this.userProfile().id, this.currentFriendChat().id)
      .subscribe({
        next: (messages) => {
          this.messageList = signal(messages);
          this.messageList().forEach(message => {
            if(message.receiverId === this.userProfile().id && message.status === MessageStatus.SENT) {
              this.chatService.markMessageAsRead(message.id).subscribe({
                next: (readMessage) => message.status = readMessage.status,
                error: (err) => this.errorDisplayer(err)
              });
            }
          })
          this.scrollToBottom();
        },
        error: (err) => this.errorDisplayer(err),
      });
  }

  handleKeydown(e:Event) {
    var event = e as KeyboardEvent;
    if(event.key === 'Enter' && !event.shiftKey) {
      this.sendMessage();
    }
  }

  sendMessage() {
    if(this.messageForm.invalid || this.currentFriendChat==undefined) return;
    const message = this.messageForm.value.message;

    if(message === undefined || message === null || message.trim().length === 0) return;

    var data = {
      senderId: this.userProfile().id,
      receiverId: this.currentFriendChat().id,
      content: message,
      status: MessageStatus.NOT_SENT
    }
    this.chatService.sendMessage(data);

    setTimeout(() => {
      this.messageForm.reset();
    }, 0);


    // set temporary messages
    const mes: Message = {
      ...data,
      id: "",
      timestamp: new Date(),
    }
    this.messageList().push(mes);
    this.scrollToBottom();
  }

  private scrollToBottom() {
    // timeout to ensure DOM changes are completed
    setTimeout(() => {
      this.chatListRef.nativeElement.scrollTop = this.chatListRef.nativeElement.scrollHeight;
    }, 10);
  }

  private bumpFriend(senderId: number) {
    this.userFriends.update(friends => {
      // 1) find the friend
      const idx = friends.findIndex(f => f.id === senderId);
      if (idx === -1) return friends;  // not in list?

      // 2) create a new FriendListUser with the notification flag
      const friend = { ...friends[idx], hasNewMessage: true };

      // 3) build a new array: move this friend to index 0
      return [
        friend,
        ...friends.slice(0, idx),
        ...friends.slice(idx + 1),
      ];
    });
  }


  errorDisplayer(err: any) {
    const exception: ExceptionResponse = err.error;
    this.alertService.pushAlert('danger', exception.detail);
  }
}

interface FriendListUser extends User{
  hasNewMessage?:boolean
}

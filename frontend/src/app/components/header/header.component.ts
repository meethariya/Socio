import { Component, inject, OnInit, Signal, signal, WritableSignal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { FriendService } from '../../services/friend.service';
import { User } from '../../models/user.model';
import { ChatService } from '../../services/chat.service';
import { combineLatest, filter, withLatestFrom } from 'rxjs';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit{
  authService = inject(AuthService);
  alertService = inject(AlertService);
  friendService = inject(FriendService);
  chatService = inject(ChatService);
  router = inject(Router);
  title = signal('Socio');
  isCollapsed: boolean = true;
  userProfile!: Signal<User>;

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        this.userProfile = signal(user);

        this.chatService.getUserUnreadMessage(user.id).subscribe({
          next: (unreadMessage) => {
            this.chatService.userUnreadMessageSenderId.set(unreadMessage);
          },
          error: (err) => {
            this.alertService.pushAlert("danger", "Error fetching unread messages");
          },
        });
        this.chatService.subscribeToMessages(user.id);
        this.chatService.getMessages().pipe(
          withLatestFrom(this.chatService.isOnChatPage()),
          filter(([message, isOnChatPage]) => !isOnChatPage)
        ).subscribe(([msg, _]) => {
          if(msg.receiverId === this.userProfile().id) {
            this.chatService.playNotification();
            this.chatService.userUnreadMessageSenderId.update((prev) => {
              // Remove senderId if it already exist
              const filtered = prev.filter(id => id !== msg.senderId);
              return [...filtered, msg.senderId];
            });
          }
        });
      },
      error: (err) => {
        this.alertService.pushAlert("info", "Login to use all functionalities");
      },
    });
  }

  toggleCollapse(): void {
    this.isCollapsed = !this.isCollapsed;
  }

  logout() {
    this.authService.clearTokens();
    this.router.navigate(['/login']);
    this.alertService.pushAlert('success', 'Successfully logged out');
  }
}

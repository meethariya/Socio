import { Component, inject, OnInit, signal, Signal } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { User } from '../../models/user.model';
import { Router, RouterLink } from '@angular/router';
import { Post } from '../../models/post.model';
import { ProfileService } from '../../services/profile.service';
import { Friendship } from '../../models/friendship.model';
import { PostComponent } from '../../components/post/post.component';
import { FriendService } from '../../services/friend.service';

@Component({
  selector: 'app-profile',
  imports: [HeaderComponent, FooterComponent, PostComponent, RouterLink],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  authService = inject(AuthService);
  alertService = inject(AlertService);
  profileService = inject(ProfileService);
  friendService = inject(FriendService);
  router = inject(Router);

  userProfile!: Signal<User>;
  userPosts!: Signal<Array<Post>>;
  userFriends!: Signal<Array<User>>;
  friendRequests!: Signal<Array<Friendship>>;

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        this.userProfile = signal(user);

        this.profileService.getPosts(this.userProfile().id)?.subscribe({
          next: (posts) => {
            this.userPosts = signal(posts);
          },
          error: (err) => {
            this.alertService.pushAlert('danger', err.detail);
          },
        });

        this.profileService.getFriends(this.userProfile().id)?.subscribe({
          next: (friends) => {
            this.userFriends = signal(friends);
            this.userFriends().forEach(f => {
              f.isFriend="FRIENDS";
            });
          },
          error: (err) => {
            this.alertService.pushAlert('danger', err.detail);
          },
        });

        this.profileService.getFriendRequests(this.userProfile().id)?.subscribe({
          next: (requests) => {
            this.friendRequests = signal(requests);
            this.friendRequests().forEach(f => {
              f.sender.isFriend="REQUEST RECEIVED"
            });
          },
          error: (err) => {
            this.alertService.pushAlert('danger', err.detail);
          },
        });
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.detail);
        this.router.navigate(['/login']);
      },
    });
  }

  requestsClick() {
    if(this.friendRequests().length==0) return;
    this.friendService.openModal(undefined,this.friendRequests()).result.then(
      (result) => {},
			(reason) => {
        if(typeof(reason) == 'object') {
          let friend = reason as User;
          if(friend.isFriend=='FRIENDS') {
            this.friendRequests = signal(this.friendRequests().filter(u => u.sender.id!=friend.id));
            this.userFriends().push(friend);
          } else if(friend.isFriend=='NOT FRIEND') {
            this.friendRequests = signal(this.friendRequests().filter(u => u.sender.id!=friend.id));
          }
        }
			},
    );
  }

  friendsClick() {
    if(this.userFriends().length==0) return;
    this.friendService.openModal(this.userFriends()).result.then(
      (results) => {},
      (reason) => {
        if(typeof(reason) == 'object') {
          let formerFriend = reason as User;
          if(formerFriend.isFriend=='NOT FRIEND') {
            this.userFriends = signal(this.userFriends().filter(u => u.id!=formerFriend.id));
          }
        }
      }
    );
  }

  copyUsername() {
    navigator.clipboard.writeText(`@${this.userProfile().username}`);
    this.alertService.pushAlert('info', 'Username copied to clipboard');
  }
}

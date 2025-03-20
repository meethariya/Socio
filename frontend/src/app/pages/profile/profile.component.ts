import { Component, inject, OnInit, signal, Signal } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { User } from '../../models/user.model';
import { Router } from '@angular/router';
import { Post } from '../../models/post.model';
import { ProfileService } from '../../services/profile.service';
import { Friendship } from '../../models/friendship.model';
import { PostComponent } from '../../components/post/post.component';
import { FriendService } from '../../services/friend.service';

@Component({
  selector: 'app-profile',
  imports: [HeaderComponent, FooterComponent, PostComponent],
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
  friendRequests!: Array<Friendship>;

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
          },
          error: (err) => {
            this.alertService.pushAlert('danger', err.detail);
          },
        });

        this.profileService.getFriendRequests(this.userProfile().id)?.subscribe({
          next: (requests) => {
            this.friendRequests = requests;
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
    this.friendService.openModal(this.friendRequests.map(f => {
      const other = f.sender;
      other.isFriend=false;
      return other}
    ));
  }

  copyUsername() {
    navigator.clipboard.writeText(`@${this.userProfile().username}`);
    this.alertService.pushAlert('info', 'Username copied to clipboard');
  }
}

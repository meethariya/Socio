import { Component, inject, OnInit, signal, Signal } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { User } from '../../models/user.model';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Post } from '../../models/post.model';
import { ProfileService } from '../../services/profile.service';
import { Friendship } from '../../models/friendship.model';
import { PostComponent } from '../../components/post/post.component';
import { FriendService } from '../../services/friend.service';
import { ExceptionResponse } from '../../models/exception-response.model';

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
  activeRoute = inject(ActivatedRoute);

  visitorProfile!: Signal<User>;
  userProfile!: Signal<User>;
  userPosts!: Signal<Array<Post>>;
  userFriends!: Signal<Array<User>>;
  friendRequests!: Signal<Array<Friendship>>;
  isSelf!: boolean;
  isFriend!:boolean;

  ngOnInit(): void {

    this.activeRoute.paramMap.subscribe(params => {
      const profileUsername = params.get("username");
      if(profileUsername == null) return;

      // if authorized
      if(this.authService.getToken() != null) {
        this.authService.getUserProfile().subscribe({
          next: (user) => {
            this.visitorProfile = signal(user);
            // if self profile
            if(this.visitorProfile().username == profileUsername) {
              this.isSelf=true;
              this.userProfile = this.visitorProfile;
              this.loadProfile(this.visitorProfile().id, this.visitorProfile().id);
            } else {
              // if other profile
              this.isSelf=false;
              this.friendService.areFriends(this.visitorProfile().username, profileUsername).subscribe({
                next: (value: any) => {
                  this.isFriend = new Map<string, boolean>(Object.entries(value)).get("friends") ?? false;
                  if(this.isFriend) {
                    // if friend profile
                    this.authService.getUser(profileUsername).subscribe({
                      next: (user) => {
                        this.userProfile = signal(user);
                        this.loadProfile(this.visitorProfile().id, this.userProfile().id);
                      },
                      error: (err) => {
                        this.alertService.pushAlert('danger', err.error.detail);
                        this.router.navigate(['/login']);
                      },
                    });
                  } else {
                    // if not friend profile
                    this.notFriendProfile(profileUsername);
                  }
                },
              })
            }
          },
          error: (err) => {
            this.alertService.pushAlert('danger', err.error.detail);
            this.router.navigate(['/login']);
          },
        });
      } else {
        // if not loggged in
        this.notFriendProfile(profileUsername);
      }
    });

  }

  notFriendProfile(profileUsername: string) {
    this.isSelf = false;
        this.isFriend = false;
        this.profileService.getProfileSummary(profileUsername).subscribe({
          next: (profile) => {
            this.userProfile = signal({
              id: profile.id,
              authId: 0,
              email: "",
              name: profile.name,
              username: profile.username,
              isFriend: 'NOT FRIEND',
              profilePic: null,
            });
            this.userFriends = signal(new Array<any>(profile.friendCount));
            this.userPosts = signal(new Array<any>(profile.postCount));
          },
          error: (err) => {
            const exception:ExceptionResponse = err.error;
            this.alertService.pushAlert("danger",exception.detail);
          }
        });
  }

  loadProfile(visitorId: number, userId: number) {
    this.profileService.getPosts(userId, visitorId)?.subscribe({
      next: (posts) => {
        this.userPosts = signal(posts);
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.error.detail);
      },
    });

    this.profileService.getFriends(userId)?.subscribe({
      next: (friends) => {
        this.userFriends = signal(friends);
        this.userFriends().forEach(f => {
          f.isFriend="FRIENDS";
        });
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.error.detail);
      },
    });

    this.profileService.getFriendRequests(userId)?.subscribe({
      next: (requests) => {
        this.friendRequests = signal(requests);
        this.friendRequests().forEach(f => {
          f.sender.isFriend="REQUEST RECEIVED"
        });
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.error.detail);
      },
    });
  }

  requestsClick() {
    if(!this.isSelf && !this.isFriend) return;
    if(this.friendRequests().length==0) return;
    this.friendService.openModal(undefined,this.friendRequests(), this.isSelf).result.then(
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
    if(!this.isSelf && !this.isFriend) return;
    if(this.userFriends().length==0) return;
    this.friendService.openModal(this.userFriends(), undefined, this.isSelf).result.then(
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

  postDelete(post: Post) {
    this.userPosts = signal(this.userPosts().filter(p => p.id!=post.id));
  }
}

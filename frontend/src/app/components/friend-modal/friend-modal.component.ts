import {
  Component, inject,
  Input, OnInit,
  signal
} from '@angular/core';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { FriendService } from '../../services/friend.service';
import { Friendship, FriendshipStatus } from '../../models/friendship.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-friend-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './friend-modal.component.html',
  styleUrl: './friend-modal.component.scss',
})
export class FriendModalComponent implements OnInit {
  alertService = inject(AlertService);
  authService = inject(AuthService);
  friendService = inject(FriendService);
  router = inject(Router);
  showNoResults = signal(false);

  userSearchResults: Array<User> = [];
  cachedFriendList: Array<User> = [];

  // cant use input() here because that's read only
  @Input() userSerachRequired!: boolean;
  @Input() inputFriendList!: Array<User>;
  @Input() friendRequests!: Array<Friendship>;
  @Input() allowCta!:boolean;

  searchForm = new FormGroup({
    search: new FormControl('', [Validators.required, Validators.minLength(3)]),
  });

  filterForm = new FormGroup({
    filter: new FormControl(''),
  });

  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    if (!this.userSerachRequired) {
      if (this.friendRequests && this.friendRequests.length) {
        this.inputFriendList = this.friendRequests.map((f) => f.sender);
      }
      this.cachedFriendList = this.inputFriendList;
      this.filterForm.get('filter')?.valueChanges.subscribe({
        next: (value) => {
          if (value == null || value.trim().length == 0)
            this.inputFriendList = this.cachedFriendList;
          else
            this.inputFriendList = this.inputFriendList.filter((f) =>
              f.name.toLowerCase().startsWith(value)
            );
          this.showNoResults = signal(this.inputFriendList.length == 0);
        },
        error: (err) => this.alertService.pushAlert('danger', err.error),
      });
    }
  }

  get safeFriendList() {
    return signal(this.inputFriendList ?? this.userSearchResults);
  }

  searchUser() {
    if (this.searchForm.invalid || this.searchForm.value.search == null) {
      this.alertService.pushAlert('danger', 'Enter username');
      return;
    }
    this.friendService.searchUser(this.searchForm.value.search).subscribe({
      next: (users) => {
        this.authService.getUserProfile().subscribe({
          next: (currentUser) => {
            users = users.filter((u) => u.id != currentUser.id);
            this.userSearchResults = users;

            if (!this.userSearchResults.length) {
              this.showNoResults.set(true);
            }
          },
          error: (err) => {
            this.alertService.pushAlert('danger', err.detail);
          },
        });
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.detail);
      },
    });
  }

  addFriend(id: number) {
    const currentUser = this.authService.userProfile;
    if (currentUser == null) return;

    const formdata = new FormData();
    formdata.append('senderId', currentUser.id.toString());
    formdata.append('receiverId', id.toString());
    this.friendService.addFriend(formdata).subscribe({
      next: (friend) => {
        const user = this.userSearchResults.find((u) => u.id == friend.receiver.id);
        if (user) {
          user.isFriend = 'REQUEST SENT';
        }
        this.alertService.pushAlert('success', `Request has been sent to ${friend.receiver.name}`);
        this.activeModal.dismiss(friend);
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.error.detail);
        this.activeModal.dismiss("cross dismiss");
      },
    });
  }

  removeFriend(userId: number) {
    this.authService.getUserProfile().subscribe({
      next: (activeUser) =>{
        this.friendService.deleteFriendship(userId, activeUser.id).subscribe({
          next: () => {
            const formerFriend = this.inputFriendList.find(u => u.id==userId);
            if(formerFriend) {
              formerFriend.isFriend = "NOT FRIEND";
              this.alertService.pushAlert("success", formerFriend.name+" removed from your friend list");
              this.activeModal.dismiss(formerFriend);
            }
          },
          error: (err) => {
            this.alertService.pushAlert("danger", err.error.detail);
          }
        });
      },
      error: (err) => {
        this.alertService.pushAlert("danger", err.error.detail);
      }
    });
  }

  approveRequest(senderId: number) {
    this.manageRequest(senderId, FriendshipStatus.ACCEPTED);
  }

  rejectRequest(senderId: number) {
    this.manageRequest(senderId, FriendshipStatus.REJECTED);
  }

  manageRequest(senderId: number, status: FriendshipStatus) {
    const friendship = this.friendRequests.find(
      (f) => (f.sender.id = senderId)
    );
    if (!friendship) return;
    this.friendService.updateFriendship(friendship.id, status).subscribe({
      next: (friendship) => {
        let message = "";
        if(status == FriendshipStatus.REJECTED) {
          message = `Request by ${friendship.sender.name} has been removed`;
          const friend = this.inputFriendList.find(u => u.id==senderId);
          if(friend) {
            friend.isFriend = 'NOT FRIEND'
            this.activeModal.dismiss(friend);
          }
        } else {
          message = `You are now friends with ${friendship.sender.name}`;
          let friend = this.inputFriendList.find(u => u.id==senderId);
          if(friend) {
            friend.isFriend="FRIENDS";
          }
          let friendShip = this.friendRequests.find(f => f.sender.id!=senderId);
          if(friendShip) {
            friendShip.sender.isFriend="FRIENDS";
          }
          this.activeModal.dismiss(friend);
        }
        this.inputFriendList = this.inputFriendList.filter(u => u.id!=senderId);
        this.friendRequests = this.friendRequests.filter(f => f.sender.id!=senderId);
        this.alertService.pushAlert('success', message);
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.error.detail);
      },
    });
  }

  openUserProfilePage(username: string) {
    this.router.navigate([`/profile/${username}`]);
    this.activeModal.dismiss("cross");
  }
}

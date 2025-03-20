import { Component, computed, inject, Input, input, signal, Signal } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';
import { FriendService } from '../../services/friend.service';

@Component({
  selector: 'app-friend-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './friend-modal.component.html',
  styleUrl: './friend-modal.component.scss'
})
export class FriendModalComponent{
  alertService = inject(AlertService);
  authService = inject(AuthService);
  friendService = inject(FriendService);
  showNoResults = signal(false);

  userSearchResults: Array<User> = [];
  @Input() inputFriendList!: Array<User>; // cant use input() here because that's read only

  constructor(public activeModal: NgbActiveModal){}


  get safeFriendList() : User[] {
    return this.inputFriendList ??this.userSearchResults;
  }

  searchForm = new FormGroup({
    search: new FormControl('', [Validators.required, Validators.minLength(3)]),
  });

  searchUser() {
    if (this.searchForm.invalid || this.searchForm.value.search == null) {
      this.alertService.pushAlert('danger', 'Enter username');
      return;
    }
    this.friendService.searchUser(this.searchForm.value.search).subscribe({
      next: (users) => {
        this.authService.getUserProfile().subscribe({
          next: (currentUser) => {
            users = users.filter(u=>u.id!=currentUser.id)
            this.userSearchResults = users;

            if(!this.userSearchResults.length) {
              this.showNoResults.set(true);
            }
          },
          error: (err) =>{
            this.alertService.pushAlert('danger', err.detail);
          }
        });
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.detail);
      },
    });
  }

  addFriend(id:number){
    const currentUser = this.authService.userProfile;
    if(currentUser==null) return

    const formdata = new FormData;
    formdata.append("senderId", currentUser.id.toString());
    formdata.append("receiverId", id.toString());
    this.friendService.addFriend(formdata).subscribe({
      next: (friend) =>{
        const user = this.userSearchResults.find(u=>u.id==id);
        if(user) {
          user.isFriend=false;
        }
      },
      error: (err) => {
        this.alertService.pushAlert("danger", err.error.detail);
      },
    });
  }

}

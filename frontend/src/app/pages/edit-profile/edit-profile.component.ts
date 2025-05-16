import { Component, inject, OnInit, signal, Signal, WritableSignal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FooterComponent } from '../../components/footer/footer.component';
import { HeaderComponent } from '../../components/header/header.component';
import { User } from '../../models/user.model';
import { AlertService } from '../../services/alert.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  imports: [HeaderComponent, FooterComponent, ReactiveFormsModule],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.scss'
})
export class EditProfileComponent implements OnInit {

  updateProfileForm = new FormGroup({
    username: new FormControl(''),
    currentPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl(''),
    email: new FormControl('', [Validators.email]),
    name: new FormControl(''),
  });
  profilePicUrl: WritableSignal<String> = signal<String>('');
  profilePicFile: File | null = null;
  authService = inject(AuthService);
  alertService = inject(AlertService);
  router = inject(Router);
  userProfile!: Signal<User>;

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        this.userProfile = signal(user);
        this.updateProfileForm.patchValue({
          username: this.userProfile().username,
          email: this.userProfile().email,
          name: this.userProfile().name,
        });
        this.updateDefaultProfilePic();
      },
      error: (err) => {
        this.alertService.pushAlert("danger", err.error.detail);
      }
    })
  }

  onFileSelected($event: Event) {
    const element = $event.target as HTMLInputElement;
    if (element.files && element.files.length > 0) {
      if(element.files[0].size > 300 * 1024) {
        this.alertService.pushAlert("danger","File size exceeds 300kb");
        return;
      }
      const file = element.files[0];
      this.profilePicFile = file;
      this.profilePicUrl.update((_) => URL.createObjectURL(file));
    } else {
      this.updateDefaultProfilePic();
    }
  }

  private updateDefaultProfilePic() {
    this.profilePicUrl.update((_) => this.userProfile().profilePic!= null ? (this.userProfile().profilePic?.includes('uploads') ? this.authService.url:'') + this.userProfile().profilePic : 'user-circle.png');
    this.profilePicFile = null;
  }

  updateProfile() {
    if(this.updateProfileForm.invalid) return;

    const formData = new FormData();
    const currentPassword = this.updateProfileForm.value.currentPassword,
      newPassword = this.updateProfileForm.value.newPassword,
      email = this.updateProfileForm.value.email,
      name = this.updateProfileForm.value.name;

    if(currentPassword && currentPassword.length) formData.append('currentPassword', currentPassword);
    if(newPassword && newPassword.length) formData.append('newPassword', newPassword);
    if(email && email.length && email!== this.userProfile().email) formData.append('email', email);
    if(name && name.length && name!== this.userProfile().name) formData.append('name', name);
    if(this.profilePicFile != null) formData.append('profilePic', this.profilePicFile);

    this.authService.updateProfile(formData, this.userProfile().id).subscribe({
      next: (user) => {
        this.authService.setUserProfile(user);
        this.alertService.pushAlert("success", "Profile updated successfully");
        this.router.navigate(['/profile', user.username]);
      },
      error: (err) => {
        this.alertService.pushAlert("danger", err.error.detail ? err.error.detail : err.message);
      }
    });

  }
}

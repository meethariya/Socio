import { Component, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { ExceptionResponse } from '../../models/exception-response.model';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-sign-up',
  imports: [ReactiveFormsModule, HeaderComponent, FooterComponent],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.scss',
})
export class SignUpComponent {
  router = inject(Router);
  authService = inject(AuthService);
  alertService = inject(AlertService);

  profilePic: File | null = null;

  signUpForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    name: new FormControl('', Validators.required),
    profilePic: new FormControl<String | null>(null),
  });

  register(): void {
    if (this.signUpForm.invalid) return;

    if (
      this.signUpForm.value.username == null ||
      this.signUpForm.value.password == null ||
      this.signUpForm.value.email == null ||
      this.signUpForm.value.name == null
    ) return;

    var formData = new FormData();
    formData.append("username", this.signUpForm.value.username);
    formData.append("password", this.signUpForm.value.password);
    formData.append("email", this.signUpForm.value.email);
    formData.append("name", this.signUpForm.value.name);
    if(this.profilePic != null) formData.append("profilePic", this.profilePic);

    this.authService.register(formData).subscribe({
      next: (user) => {
        this.alertService.pushAlert("success","Account created successfully. Kindly continue to login with username: "+user.username);
        this.router.navigate(["/login"]);
      },
      error: (err) => {
        const exception:ExceptionResponse = err.error;
        this.alertService.pushAlert("danger",exception.detail);
      }
    });
  }

  onProfileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      if(input.files[0].size > 300 * 1024) {
        this.alertService.pushAlert("danger","File size exceeds 300kb");
        this.profilePic = null;
        this.signUpForm.patchValue({ profilePic: null });
        return;
      }
      this.profilePic = input.files[0];
    } else {
      this.profilePic = null;
      this.signUpForm.patchValue({ profilePic: null });
    }
  }
}

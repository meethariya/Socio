import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ExceptionResponse } from '../../models/exception-response.model';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-login',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  authService = inject(AuthService);
  alertService = inject(AlertService);
  router = inject(Router);

  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    rememberMe: new FormControl(false),
  });

  login(): void {
    if (this.loginForm.invalid) return;
    if (
      this.loginForm.value.username == null ||
      this.loginForm.value.password == null
    )return;

    let formData = new FormData();
    const username = this.loginForm.value.username;
    formData.append('username', username);
    formData.append('password', this.loginForm.value.password);

    this.authService.login(formData).subscribe({
      next: (token: string) => {
        this.authService.setToken(token, this.loginForm.value.rememberMe == null ? false : this.loginForm.value.rememberMe);

        // set user profile
        this.authService.getUser(username).subscribe({
          next: (user) => {this.authService.setUserProfile(user)},
          error: (err) => {
            const exception:ExceptionResponse = err.error;
            this.alertService.pushAlert("danger",exception.detail);
          }
        });

        this.alertService.pushAlert("success","Successfully logged in");
        this.router.navigate(['/']);
      },
      error: (err) => {
        if (typeof err.error === 'string') {
          const exception:ExceptionResponse = JSON.parse(err.error) as ExceptionResponse;
          this.alertService.pushAlert("danger",exception.detail);
        } else{
          this.alertService.pushAlert("danger",err.error);
        }
      },
    });
  }
}

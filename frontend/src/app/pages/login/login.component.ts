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

@Component({
  selector: 'app-login',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  authService = inject(AuthService);
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
    formData.append('username', this.loginForm.value.username);
    formData.append('password', this.loginForm.value.password);

    this.authService.login(formData).subscribe({
      next: (token: string) => {
        this.authService.setToken(token, this.loginForm.value.rememberMe == null ? false : this.loginForm.value.rememberMe);
        console.log("Logged In");
        this.router.navigate(['/']);
      },
      error(err) {
        if (typeof err.error === 'string') {
          err = JSON.parse(err.error) as ExceptionResponse;
        }
        console.error(err);
      },
    });
  }
}

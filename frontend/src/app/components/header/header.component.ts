import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';

@Component({
  selector: 'app-header',
  imports: [RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  authService = inject(AuthService);
  alertService = inject(AlertService);
  router = inject(Router);
  title = signal("Socio");

  logout(){
    this.authService.clearTokens();
    this.router.navigate(["/login"]);
    this.alertService.pushAlert("success","Successfully logged out");
  }
}

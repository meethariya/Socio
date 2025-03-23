import { Component, inject, input } from '@angular/core';
import { Post } from '../../models/post.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-post',
  imports: [],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  authService = inject(AuthService);
  post= input.required<Post>();
  username = input.required<string>();
}

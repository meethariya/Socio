import { Component, input } from '@angular/core';
import { Post } from '../../models/post.model';

@Component({
  selector: 'app-post',
  imports: [],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  post= input.required<Post>();
  username = input.required<string>();
}

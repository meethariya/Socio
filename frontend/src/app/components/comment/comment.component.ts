import { Component, inject, input, OnInit, output } from '@angular/core';
import { Comment } from '../../models/comment.model';
import { TimeAgoPipe } from '../../pipes/time-ago.pipe';
import { AuthService } from '../../services/auth.service';
import { RouterLink } from '@angular/router';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-comment',
  imports: [TimeAgoPipe, RouterLink, ReactiveFormsModule],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.scss',
})
export class CommentComponent implements OnInit {
  comment = input.required<Comment>();
  isSelf = false;
  isEditing = false;
  authService = inject(AuthService);
  commentDelete = output<Comment>();
  commentUpdate = output<{ comment: Comment; content: string }>();
  commentForm = new FormGroup({
    content: new FormControl('', [Validators.required]),
  });

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        this.isSelf = user.username == this.comment().username;
      },
    });
  }

  editComment() {
    this.isEditing = true;
    this.commentForm.patchValue({
      content: this.comment().content,
    });
  }

  deleteComment() {
    this.commentDelete.emit(this.comment());
  }

  submitForm() {
    if (this.commentForm.invalid) return;

    const content = this.commentForm.value.content;
    if (content == undefined || content == null || content.length == 0) return;

    this.commentUpdate.emit({
      comment: this.comment(),
      content: content,
    });
    this.isEditing = false;
  }
}

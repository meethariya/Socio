import { Component, inject, input, OnInit, output, signal, TemplateRef } from '@angular/core';
import { Post } from '../../models/post.model';
import { AuthService } from '../../services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PostService } from '../../services/post.service';
import { AlertService } from '../../services/alert.service';
import { Router, RouterLink } from '@angular/router';
import { PluralPipe } from '../../pipes/plural.pipe';
import { Comment } from '../../models/comment.model';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ExceptionResponse } from '../../models/exception-response.model';
import { CommentComponent } from "../comment/comment.component";
import { TimeAgoPipe } from '../../pipes/time-ago.pipe';

@Component({
  selector: 'app-post',
  imports: [RouterLink, PluralPipe, ReactiveFormsModule, CommentComponent, TimeAgoPipe],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  post = input.required<Post>();
  username = input.required<string>();
  canEdit = input<boolean>(false);
  postDelete = output<Post>();
  showOverlay = false;
  showComments = false;
  comments = signal(Array<Comment>(0));

  commentForm = new FormGroup({
    content: new FormControl('',[Validators.required])
  });

  authService = inject(AuthService);
  modalService = inject(NgbModal);
  postService = inject(PostService);
  alertService = inject(AlertService);
  router = inject(Router);

  openDeleteModal(content: TemplateRef<any>){
    this.modalService.open(content, {centered: true}).result.then(
      (result:string) => {
        if(result=='Delete') {
          this.postService.deletePost(this.post().id).subscribe({
            next: () =>{
              this.alertService.pushAlert("success", "Post has been deleted successfully!");
              this.postDelete.emit(this.post());
            },
            error: (err) => {
              this.alertService.pushAlert("danger", err.error.detail);
            }
          });
        }
			},
      (reason) => {}
    );
  }

  likeUnlikePost() {
    this.post().likedByVisitor = !this.post().likedByVisitor;
    if(this.post().likedByVisitor) {
      this.post().likeCount += 1;
    } else {
      this.post().likeCount -= 1;
    }
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        let formData = new FormData();
        formData.append("postId", this.post().id);
        formData.append("userId", user.id.toString());
        this.postService.likeUnlikePost(formData, this.post().likedByVisitor??false).subscribe({});
      }
    });
  }

  loadComments(){
    this.showOverlay = false;
    this.showComments = true;

    this.postService.getCommentsOfPost(this.post().id).subscribe({
      next: (comments) => {
        this.comments = signal(comments);
      },
      error: (err) => {
        const error = err.error as ExceptionResponse;
        this.alertService.pushAlert("danger", error.detail);
      }
    });
  }

  addComment(){
    if(this.commentForm.invalid) return;

    const content = this.commentForm.value.content;
    if(content == undefined || content == null || content.length == 0) return;

    this.authService.getUserProfile().subscribe({
      next: (user) => {
        const formData = new FormData();
        formData.append("postId",this.post().id);
        formData.append("username",user.username);
        formData.append("content", content);

        this.postService.addComment(formData).subscribe({
          next: (comment) => {
            this.commentForm.reset();
            this.comments().push(comment);
            this.post().commentCount+=1;
            this.alertService.pushAlert("success", "New comment added");
          }
        });
      },
      error: (err) => {
        const error = err.error as ExceptionResponse;
        this.alertService.pushAlert("danger", error.detail);
        this.router.navigate(['/login']);
      }
    });

  }

  deleteComment(comment: Comment) {
    this.postService.deleteComment(comment.id).subscribe({
      next: () => {
        this.comments.set(this.comments().filter(c => c.id != comment.id));
        this.post().commentCount -= 1;
        this.alertService.pushAlert("success", "Comment removed");
      },
      error: (err) => {
        const error = err.error as ExceptionResponse;
        this.alertService.pushAlert("danger", error.detail);
      }
    });
  }

  editComment(obj:{comment: Comment, content: string}) {
    const comment = obj.comment;
    const content = obj.content;

    const formData = new FormData();
    formData.append("content", content);
    this.postService.updateComment(formData, comment.id).subscribe({
      next: (comment) => {
        this.comments.set(this.comments().map(c => c.id !== comment.id ? c : comment));
        this.alertService.pushAlert("success", "Comment updated");
      },
      error: (err) => {
        const error = err.error as ExceptionResponse;
        this.alertService.pushAlert("danger", error.detail);
      }
    });
  }

  redirectToProfile() {
    this.router.navigate([`/profile/${this.username()}`]);
  }
}

import { Component, inject, input, output, signal, TemplateRef } from '@angular/core';
import { Post } from '../../models/post.model';
import { AuthService } from '../../services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PostService } from '../../services/post.service';
import { AlertService } from '../../services/alert.service';
import { RouterLink } from '@angular/router';
import { PluralPipe } from '../../pipes/plural.pipe';

@Component({
  selector: 'app-post',
  imports: [RouterLink, PluralPipe],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  post= input.required<Post>();
  username = input.required<string>();
  canEdit = input<boolean>(false);
  postDelete = output<Post>();
  showOverlay = false;

  authService = inject(AuthService);
  modalService = inject(NgbModal);
  postService = inject(PostService);
  alertService = inject(AlertService);

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
      this.post().likeCount+=1;
    } else {
      this.post().likeCount-=1;
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
}

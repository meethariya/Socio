import { Component, inject, input, output, signal, TemplateRef } from '@angular/core';
import { Post } from '../../models/post.model';
import { AuthService } from '../../services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PostService } from '../../services/post.service';
import { AlertService } from '../../services/alert.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-post',
  imports: [RouterLink],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss'
})
export class PostComponent {
  authService = inject(AuthService);
  post= input.required<Post>();
  username = input.required<string>();
  canEdit = input<boolean>(false);
  postDelete = output<Post>();
  showOverlay = false;

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
}

import {
  Component,
  ElementRef,
  inject,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { PostComponent } from '../../components/post/post.component';
import { Post } from '../../models/post.model';
import { User } from '../../models/user.model';
import { AuthService } from '../../services/auth.service';
import { AlertService } from '../../services/alert.service';
import { PostService } from '../../services/post.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ExceptionResponse } from '../../models/exception-response.model';

@Component({
  selector: 'app-post-editor',
  imports: [
    HeaderComponent,
    FooterComponent,
    ReactiveFormsModule,
    PostComponent,
  ],
  templateUrl: './post-editor.component.html',
  styleUrl: './post-editor.component.scss',
})
export class PostEditorComponent implements OnInit {
  authService = inject(AuthService);
  alertService = inject(AlertService);
  postService = inject(PostService);
  router = inject(Router);
  activeRoute = inject(ActivatedRoute);

  imageLabelText = signal('Drag and drop your image or browse');
  isUpdate = false;
  backgroundImage: string | null = null;
  imageFile: File | null = null;
  post: Post = {
    id: '0',
    caption: '',
    location: '',
    imageUrl: '',
    userId: 1,
    covered: false,
    needBlurBackground: false,
    timestamp: new Date()
  };
  user!: User;

  @ViewChild('imageLabel') imageLabel!: ElementRef;
  @ViewChild('imageInput') imageInput!: ElementRef;

  postForm = new FormGroup({
    image: new FormControl(null, Validators.required),
    caption: new FormControl(''),
    location: new FormControl(''),
    covered: new FormControl(false),
    needBlurBackground: new FormControl(false),
  });

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        this.user = user;

        this.activeRoute.paramMap.subscribe(params=>{
          const postId = params.get("id");
          if(postId) {
            this.postService.getPostById(postId).subscribe({
              next: (post) => {
                if(post.userId != this.user.id){
                  this.alertService.pushAlert("danger","You are not owner of this post");
                  return;
                }
                this.isUpdate = true;

                this.post = post;
                this.postForm.patchValue({
                  caption: post.caption,
                  location: post.location,
                  covered: post.covered,
                  needBlurBackground: post.needBlurBackground,
                });
                this.postForm.controls['image'].disable();

                this.backgroundImage = post.imageUrl;
                this.postForm.updateValueAndValidity();
              },
            });
          }
        });

      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.error.detail);
      },
    });

    this.postForm.valueChanges.subscribe((value) => {
      this.post = {
        id: this.post.id,
        caption: value.caption ?? '',
        location: value.location ?? '',
        imageUrl: this.backgroundImage??'',
        userId: this.user.id,
        covered: value.covered ?? false,
        needBlurBackground: value.needBlurBackground ?? false,
        timestamp: new Date()
      };
    });
  }

  imageDragOver(event: Event) {
    event.preventDefault();

    this.imageLabel.nativeElement.classList.add('drag-enter');
    this.imageLabelText.update((v) => 'Drop!');
  }

  imageDragLeave(event: Event) {
    event.preventDefault();

    this.imageLabel.nativeElement.classList.remove('drag-enter');
    this.imageLabelText.update((v) => 'Drag and drop your image or browse');
  }

  imageDrop(event: DragEvent) {
    this.imageDragLeave(event);

    if (event.dataTransfer) {
      this.imageInput.nativeElement.files = event.dataTransfer.files;
      this.uploadImage();
    }
  }

  uploadImage() {
    if (this.imageInput.nativeElement.files && this.imageInput.nativeElement.files.length > 0) {
      const file = this.imageInput.nativeElement.files[0];

      this.imageFile = file;

      this.backgroundImage = URL.createObjectURL(file);
      this.imageLabelText.update((v) => `${file.name} added, Click to update`);
    } else {
      this.backgroundImage = null;
      this.imageFile = null;
      this.imageLabelText.update((v) => 'Drag and drop your image or browse');
    }
    this.postForm.updateValueAndValidity();
  }

  submitPost() {
    if(this.postForm.invalid || this.imageFile==null) return;

    var formData = new FormData();
    formData.append("image",this.imageFile);

    if(this.postForm.value.caption != null && this.postForm.value.caption.length > 0) {
      formData.append("caption",this.postForm.value.caption);
    }

    if(this.postForm.value.location != null && this.postForm.value.location.length > 0) {
      formData.append("location",this.postForm.value.location);
    }

    formData.append("userId", this.user.id.toString());
    formData.append("covered", (this.postForm.value.covered??false).toString());
    formData.append("needBlurBackground", (this.postForm.value.needBlurBackground??false).toString());

    this.postService.createPost(formData).subscribe({
      next: (post) => {
        this.alertService.pushAlert("success", "Post uploaded successfully");
        this.router.navigate(["/profile"]);
      },
      error: (err) => {
        this.alertService.pushAlert("danger", err.error.detail);
      }
    });
  }

  updatePost(){
    var formData = new FormData();

    if(this.postForm.value.caption != null) {
      formData.append("caption",this.postForm.value.caption);
    }

    if(this.postForm.value.location != null) {
      formData.append("location",this.postForm.value.location);
    }

    formData.append("covered", (this.postForm.value.covered??false).toString());
    formData.append("needBlurBackground", (this.postForm.value.needBlurBackground??false).toString());

    this.postService.updatePost(this.post.id, formData).subscribe({
      next: (post) => {
        this.alertService.pushAlert("success", "Post updated successfully");
        this.router.navigate(["/profile"]);
      },
      error: (err) => {
        const error = err.error as ExceptionResponse;
        this.alertService.pushAlert("danger", error.detail);
      }
    });
  }
}

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
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-post',
  imports: [
    HeaderComponent,
    FooterComponent,
    ReactiveFormsModule,
    PostComponent,
  ],
  templateUrl: './create-post.component.html',
  styleUrl: './create-post.component.scss',
})
export class CreatePostComponent implements OnInit {
  authService = inject(AuthService);
  alertService = inject(AlertService);
  postService = inject(PostService);
  router = inject(Router);

  imageLabelText = signal('Drag and drop your image or browse');
  backgroundImage: string | null = null;
  imageFile: File | null = null;
  post: Post;
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

  constructor() {
    this.authService.getUserProfile().subscribe({
      next: (user) => {
        this.user = user;
      },
      error: (err) => {
        this.alertService.pushAlert('danger', err.error.detail);
      },
    });
    this.post = {
      id: '0',
      caption: '',
      location: '',
      imageUrl: '',
      userId: 1,
      covered: false,
      needBlurBackground: false
    };
  }
  ngOnInit(): void {
    this.postForm.valueChanges.subscribe((value) => {
      this.post = {
        id: '0',
        caption: value.caption ?? '',
        location: value.location ?? '',
        imageUrl: this.backgroundImage??'',
        userId: this.user.id,
        covered: value.covered ?? false,
        needBlurBackground: value.needBlurBackground ?? false
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
      this.uploadImage(event);
    }
  }

  uploadImage(event: Event) {
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
}

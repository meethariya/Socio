import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/home/home.component')).HomeComponent
  },
  {
    path: 'login',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/login/login.component')).LoginComponent
  },
  {
    path: 'sign-up',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/sign-up/sign-up.component')).SignUpComponent
  },
  {
    path: 'profile',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/profile/profile.component')).ProfileComponent
  },
  {
    path: 'create-post',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/create-post/create-post.component')).CreatePostComponent
  }
];

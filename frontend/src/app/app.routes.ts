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
    path: 'profile/:username',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/profile/profile.component')).ProfileComponent
  },
  {
    path: 'post-editor',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/post-editor/post-editor.component')).PostEditorComponent
  },
  {
    path: 'post-editor/:id',
    pathMatch: "full",
    loadComponent: async () => (await import('./pages/post-editor/post-editor.component')).PostEditorComponent
  }
];

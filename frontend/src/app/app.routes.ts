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
  }
];

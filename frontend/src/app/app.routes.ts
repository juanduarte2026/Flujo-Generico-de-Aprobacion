import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { CreateRequestComponent } from './features/requests/create-request/create-request.component';
import { RequestListComponent } from './features/requests/request-list/request-list.component';
import { RequestDetailComponent } from './features/requests/request-detail/request-detail.component';
import { PendingRequestsComponent } from './features/requests/pending-requests/pending-requests.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'requests/create',
    component: CreateRequestComponent,
    canActivate: [authGuard]
  },
  {
    path: 'requests/list',
    component: RequestListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'requests/pending',
    component: PendingRequestsComponent,
    canActivate: [authGuard]
  },
  {
    path: 'requests/detail/:id',
    component: RequestDetailComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '/dashboard' }
];
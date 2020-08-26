import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { UsersComponent } from './components/users/users.component';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { UserprofileComponent } from './components/userprofile/userprofile.component';
import { UsersettingsComponent } from './components/usersettings/usersettings.component';
import { HostaccommodationsComponent } from "./components/hostaccommodations/hostaccommodations.component";
import { AccommodationComponent } from "./components/accommodation/accommodation.component";
import { HostchatsComponent } from './components/hostchats/hostchats.component';
import { AccommodationchatsComponent } from './components/accommodationchats/accommodationchats.component';
import { GuestchatsComponent } from './components/guestchats/guestchats.component';
import { ChatComponent } from './components/chat/chat.component';
import { SearchComponent } from './components/search/search.component';
import { AccommodationsettingsComponent } from './components/accommodationsettings/accommodationsettings.component';

import { AuthGuard } from './guards/auth.guard';
import { SignupGuard } from './guards/signup.guard';
import { AdminGuard } from './guards/admin.guard';
import { HostGuard } from './guards/host.guard';
import { GuestGuard } from './guards/guest.guard';
import { ChatGuard } from './guards/chat.guard';

const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'search', component: SearchComponent },
  {
    path: 'users',
    children: [
      { path: '', component: UsersComponent, canActivate: [AuthGuard, AdminGuard] },
      { path: ':username', component: UserprofileComponent, canActivate: [AdminGuard] }
    ]
  },
  {
    path: 'accommodations',
    children: [
      { path: '', component: HostaccommodationsComponent, canActivate: [AuthGuard, HostGuard] },
      { path: ':id', component: AccommodationComponent }
    ]
  },
  { path: 'accommodationsettings/:id', component: AccommodationsettingsComponent, canActivate: [AuthGuard, HostGuard] },
  { path: 'hostchats', component: HostchatsComponent, canActivate: [AuthGuard, HostGuard] },
  { path: 'accommodationchats/:id', component: AccommodationchatsComponent, canActivate: [AuthGuard, HostGuard] },
  { path: 'guestchats', component: GuestchatsComponent, canActivate: [AuthGuard, GuestGuard] },
  { path: 'chat/:id/:guestUsername', component: ChatComponent, canActivate: [AuthGuard, ChatGuard] },
  { path: 'settings', component: UsersettingsComponent, canActivate: [AuthGuard] },
  { path: 'signup', component: SignupComponent, canActivate: [SignupGuard] },
  { path: 'login', component: LoginComponent },
  ];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

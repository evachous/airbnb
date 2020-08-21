import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { UserComponent } from './components/user/user.component';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { UserprofileComponent } from './components/userprofile/userprofile.component';
import { UsersettingsComponent } from './components/usersettings/usersettings.component';
import { HostaccommodationsComponent } from "./components/hostaccommodations/hostaccommodations.component";
import { AccommodationComponent } from "./components/accommodation/accommodation.component";
import { AuthGuard } from './guards/auth.guard';
import { SignupGuard } from './guards/signup.guard';
import { AdminGuard } from './guards/admin.guard';
import { HostGuard } from './guards/host.guard';

const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'users',
    children: [
      { path: '', component: UserComponent, canActivate: [AdminGuard] },
      { path: ':username', component: UserprofileComponent, canActivate: [AdminGuard] }
    ]
  },
  {
    path: 'accommodations',
    children: [
      { path: '', component: HostaccommodationsComponent, canActivate: [HostGuard] },
      { path: ':id', component: AccommodationComponent }
    ]
  },
  { path: 'settings', component: UsersettingsComponent, canActivate: [AuthGuard] },
  { path: 'signup', component: SignupComponent, canActivate: [SignupGuard] },
  { path: 'login', component: LoginComponent },
  ];

@NgModule({
  imports: [RouterModule.forRoot(appRoutes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

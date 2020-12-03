import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { UsersComponent } from './components/users/users.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { JwtInterceptor } from './helpers/jwt.interceptor';
import { ErrorInterceptor } from './helpers/error.interceptor';
import { DataService } from './services/data.service';
import { AuthGuard } from './guards/auth.guard';
import { UserprofileComponent } from './components/userprofile/userprofile.component';
import { UsersettingsComponent } from './components/usersettings/usersettings.component';
import { HostaccommodationsComponent } from './components/hostaccommodations/hostaccommodations.component';
import { AccommodationComponent } from './components/accommodation/accommodation.component';
import { ChatComponent } from './components/chat/chat.component';
import { HostchatsComponent } from './components/hostchats/hostchats.component';
import { AccommodationchatsComponent } from './components/accommodationchats/accommodationchats.component';
import { GuestchatsComponent } from './components/guestchats/guestchats.component';
import { SearchComponent } from './components/search/search.component';
import { AccommodationsettingsComponent } from './components/accommodationsettings/accommodationsettings.component';
import { ModalComponent } from './components/modal/modal.component';
import { GuestreservationsComponent } from './components/guestreservations/guestreservations.component';
import { HostreservationsComponent } from './components/hostreservations/hostreservations.component';
import { AccommodationreservationsComponent } from './components/accommodationreservations/accommodationreservations.component';
import { ScrolltotopComponent } from './components/scrolltotop/scrolltotop.component';

@NgModule({
  declarations: [
    AppComponent,
    UsersComponent,
    HomeComponent,
    NavbarComponent,
    SignupComponent,
    LoginComponent,
    UserprofileComponent,
    UsersettingsComponent,
    HostaccommodationsComponent,
    AccommodationComponent,
    ChatComponent,
    HostchatsComponent,
    AccommodationchatsComponent,
    GuestchatsComponent,
    SearchComponent,
    AccommodationsettingsComponent,
    ModalComponent,
    GuestreservationsComponent,
    HostreservationsComponent,
    AccommodationreservationsComponent,
    ScrolltotopComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    LeafletModule
  ],
  providers: [
    AuthGuard,
    DataService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

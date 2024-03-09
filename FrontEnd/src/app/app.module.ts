import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { SessionComponent } from './session/session.component';
import { RestaurantComponent } from './restaurant/restaurant.component';
import { HttpClientModule } from '@angular/common/http';
import {FormsModule} from "@angular/forms";
import {SessionDataService} from "./session-data.service";


@NgModule({
  declarations: [
    HomeComponent,
    SessionComponent,
    RestaurantComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [SessionDataService],
  bootstrap: [AppComponent]
})
export class AppModule { }

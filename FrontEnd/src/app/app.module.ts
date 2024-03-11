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
import { ResultComponent } from './result/result.component';
import { InvalidSessionPopupComponent } from './invalid-session-popup/invalid-session-popup.component';
import { CommonErrorPopupComponent } from './common-error-popup/common-error-popup.component';
import {ClipboardModule} from "ngx-clipboard";

@NgModule({
  declarations: [
    HomeComponent,
    SessionComponent,
    RestaurantComponent,
    ResultComponent,
    InvalidSessionPopupComponent,
    CommonErrorPopupComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        ClipboardModule
    ],
  providers: [ SessionDataService ],
  bootstrap: [AppComponent]
})
export class AppModule { }

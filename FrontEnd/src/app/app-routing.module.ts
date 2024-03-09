import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./home/home.component";
import { SessionComponent } from "./session/session.component";

const routes: Routes = [
  { path: 'session/:sessionCode', component: SessionComponent },
  { path: 'home', component: HomeComponent },
  { path: '**', redirectTo: '/home', pathMatch: 'full' },
  { path: '', redirectTo: '/home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

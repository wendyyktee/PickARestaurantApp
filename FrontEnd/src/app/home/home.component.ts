import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Router } from '@angular/router';
import {Session} from "../session/session.model";
import { SessionDataService } from "../session-data.service"

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(private http: HttpClient, private router:Router, private dataService: SessionDataService) {}

  startNewSession() {
    this.http
      .get<any>('http://localhost:8080/session/initiateSession')
      .subscribe(data => {
        if(data.sessionCode != null){
          this.dataService.setIsInitiator(true);
          this.router.navigate(['/session', data.sessionCode])
        }
      });
  }
}

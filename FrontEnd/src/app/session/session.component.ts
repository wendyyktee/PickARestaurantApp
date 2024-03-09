import {Component, Input} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Session} from "./session.model";
import {SessionDataService} from "../session-data.service";

@Component({
  selector: 'app-session',
  templateUrl: './session.component.html',
  styleUrl: './session.component.css'
})

export class SessionComponent {
  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router,  private dataService: SessionDataService) {
  }

  isValidSession: boolean = false;

  ngOnInit() {
    console.log(this.dataService.getSession())
    this.route.params.subscribe(params => {
      this.dataService.setSessionCode(params['sessionCode']);
      this.validateSession();
    });
  };

  validateSession() {
    return this.http
      .get<any>('http://localhost:8080/session/' + this.dataService.session.sessionCode)
      .subscribe(response => {
          this.isValidSession = true
          this.dataService.setId(response.id);
          this.dataService.setSessionCode(response.sessionCode);
          this.dataService.setStatus(response.status);
        },
        (e: HttpErrorResponse) => {
          console.log('validateSession call failed')
          console.log(e.status)
          this.isValidSession = false
        });
  }
}

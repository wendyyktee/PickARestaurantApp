import {Component} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {SessionDataService} from "../session-data.service"
import {CommonErrorPopupService} from "../common-error-popup/common-error-popup.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(private http: HttpClient, private router: Router, private dataService: SessionDataService,
              private commonErrorPopupService: CommonErrorPopupService) {
  }

  startNewSession() {
    this.http
      .get<any>('http://localhost:8080/session/initiateSession')
      .subscribe(data => {
        if (data.id != null) {
          localStorage.setItem('userSessionId', data.initiatorUserSessionId);

          this.dataService.setInitiatorUserSessionId(data.initiatorUserSessionId);
          this.dataService.setId(data.id);
          this.router.navigate(['/session', data.id])
        }
      }),
      (e: HttpErrorResponse) => {
        console.log('validateSession call failed');
        console.log(e.status);
        console.log(e.message);
        this.commonErrorPopupService.openPopup("Unexpected Error", "Please try again later or contact Administrator.")
      };
  }
}

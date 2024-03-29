import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {SessionDataService} from "../session-data.service";
import {CommonErrorPopupService} from "../common-error-popup/common-error-popup.service";
import {InvalidSessionPopupService} from "../invalid-session-popup/invalid-session-popup.service";
import { ClipboardModule } from 'ngx-clipboard';

@Component({
  selector: 'app-session',
  templateUrl: './session.component.html',
  styleUrl: './session.component.css'
})

export class SessionComponent {
  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router,
              private dataService: SessionDataService, private invalidServicePopupService: InvalidSessionPopupService,
              private commonErrorPopupService: CommonErrorPopupService) {
  }

  userSessionId = localStorage.getItem('userSessionId') as string;
  isValidSession: boolean = false;
  isInitiator: boolean = false;
  sessionUrl: string = "";

  ngOnInit() {
    this.route.params.subscribe(params => {
      const sessionId = params['sessionId'];

      this.dataService.setId(sessionId);
      this.validateSession();
    });
    this.sessionUrl = location.href;
  };

  checkIsInitiator() {
    if (this.userSessionId != null && this.userSessionId == this.dataService.getSession().initiatorUserSessionId) {
      this.isInitiator = true;
    }
  }

  validateSession() {
    return this.http
      .get<any>('http://localhost:8080/session/' + this.dataService.session.id)
      .subscribe(response => {
          this.isValidSession = true;

          this.dataService.setId(response.id);
          this.dataService.setStatus(response.status);
          this.dataService.setInitiatorUserSessionId(response.initiatorUserSessionId);

          this.checkIsInitiator();

          if(response.status == 'CLOSED'){
            this.router.navigate(['/result', response.id])
          }
        },
        (e: HttpErrorResponse) => {
          console.log('validateSession call failed')
          console.log(e.status)
          this.isValidSession = false;

          if(e && e.status == 400){//BAD_REQUEST
              this.invalidServicePopupService.openPopup("Invalid Session", "The session is invalid.")
          }
          else{
              this.invalidServicePopupService.openPopup("Unexpected Error", "Please try again later or contact Administrator.")
          }
        });
  }

  endSession() {
    const params = new HttpParams().append('userSessionId', this.userSessionId);

    return this.http
      .get<any>('http://localhost:8080/session/endSession/' + this.dataService.session.id, {params})
      .subscribe(response => {
          localStorage.removeItem("userSessionId");
          this.router.navigate(['/result', this.dataService.session.id])
        },
        (e: HttpErrorResponse) => {
          console.log('validateSession call failed')
          console.log(e.status)
          if(e){
            if(e.status == 400){//BAD_REQUEST
              this.invalidServicePopupService.openPopup("Session expired", "The session is no longer Active.")
            }
            else if (e.status == 401){
              this.commonErrorPopupService.openPopup("Unauthorized", "Only initiator is allowed to end session.")
            }
            else{
              this.invalidServicePopupService.openPopup("Unexpected Error", "Please try again later or contact Administrator.")
            }
          }
          else{
              this.invalidServicePopupService.openPopup("Unexpected Error", "Please try again later or contact Administrator.")
          }
        });
  }
}

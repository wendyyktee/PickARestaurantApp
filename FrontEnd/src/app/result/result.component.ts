import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {CommonErrorPopupService} from "../common-error-popup/common-error-popup.service";

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrl: './result.component.css',
})
export class ResultComponent {
  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router,  private commonErrorPopupService: CommonErrorPopupService) {}

  sessionCode: string = "";
  pickedRestaurantName: string = "";

  ngBeforeViewInit() {
    this.route.params.subscribe(params => {
      this.sessionCode = params['sessionCode']
      this.getResult();
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.sessionCode = params['sessionCode']
      this.getResult();
    });
  };

  getResult() {
    return this.http
      .get<any>('http://localhost:8080/result/' + this.sessionCode)
      .subscribe(response => {
          this.pickedRestaurantName = response.pickedRestaurantName;
        },
        (e: HttpErrorResponse) => {
          console.log('validateSession call failed')
          console.log(e.status)
          if(e && e.status == 400){//BAD_REQUEST
            this.commonErrorPopupService.openPopup("Invalid Session", "The session is invalid.")
          }
          else{
            this.commonErrorPopupService.openPopup("Unexpected Error", "Please try again later or contact Administrator.")
          }
        });
  }

  backToHome() {
    this.router.navigate(['/home'])
  }
}

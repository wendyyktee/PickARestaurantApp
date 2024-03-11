import {Component, ElementRef, QueryList, ViewChildren} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {Restaurant} from "./restaurant.model";
import {SessionDataService} from "../session-data.service";
import {interval, Subscription} from 'rxjs';
import {CommonErrorPopupService} from "../common-error-popup/common-error-popup.service";

@Component({
    selector: 'app-restaurant',
    templateUrl: './restaurant.component.html',
    styleUrl: './restaurant.component.css'
})
export class RestaurantComponent {
    constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router, private dataService: SessionDataService,
                private commonErrorPopupService: CommonErrorPopupService) {
    }

    subscription: Subscription;
    session = this.dataService.getSession();
    @ViewChildren("restaurantListDiv") restaurantListDiv: QueryList<ElementRef>;

    public restaurantToAdd: string = "";
    restaurants: Restaurant[];

    ngOnInit() {
        console.log(this.session)
        this.getRestaurantList()

        //Refresh list every 30 seconds
        const source = interval(30000);
        this.subscription = source.subscribe(val => this.getRestaurantList());
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    ngAfterViewInit() {
        this.restaurantListDiv.changes.subscribe(() => {
            if (this.restaurantListDiv && this.restaurantListDiv.last) {
                this.restaurantListDiv.last.nativeElement.focus();
            }
        });
    }


    getRestaurantList() {
        console.log('getRestaurantList function')
        const params = new HttpParams().append('sessionId', this.session.id);
        return this.http
            .get<Restaurant[]>('http://localhost:8080/restaurant', {params})
            .subscribe(res => {
                    this.restaurants = res;
                },
                (e: HttpErrorResponse) => {
                    console.log('validateSession call failed')
                    console.log(e.status)
                });
    }

    addRestaurant() {
        console.log('getRestaurantList function')
        const restaurant = {
            restaurantName: this.restaurantToAdd,
            sessionId: this.session.id
        }

        return this.http
            .put<any>('http://localhost:8080/restaurant/addRestaurant', restaurant)
            .subscribe(res => {
                    console.log('addRestaurant call')
                    this.restaurantToAdd = "";
                    this.getRestaurantList();
                },
                (e: HttpErrorResponse) => {
                    console.log('addRestaurant call failed')
                    console.log(e.status)

                    if (e && e.status == 400) {//BAD_REQUEST
                        this.commonErrorPopupService.openPopup("Invalid Session", "The session is invalid, please make sure the url is" +
                            " correct or try refresh your browser")
                    } else {
                        this.commonErrorPopupService.openPopup("Unexpected Error", "Please try again later or contact Administrator.")
                    }
                });
    }
}

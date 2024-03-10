import {Injectable} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {CommonErrorPopupComponent} from "./common-error-popup.component";

@Injectable({
    providedIn: 'root'
})
export class CommonErrorPopupService {
    constructor(private dialog: MatDialog) {
    }

    openPopup(title: string, message: string) {
        this.dialog.open(CommonErrorPopupComponent, {
            panelClass: 'my-custom-panel',
            data: {dialogTitle: title, dialogText: message}
        });
    }
}

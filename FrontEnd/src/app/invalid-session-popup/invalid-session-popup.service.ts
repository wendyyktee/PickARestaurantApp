import {Inject, Injectable} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {InvalidSessionPopupComponent} from "./invalid-session-popup.component";
import {CommonErrorPopupComponent} from "../common-error-popup/common-error-popup.component";

@Injectable({
  providedIn: 'root'
})
export class InvalidSessionPopupService {

  constructor(private dialog: MatDialog) {}

    openPopup(title:string, message:string) {
        this.dialog.open(InvalidSessionPopupComponent, {
            panelClass: 'my-custom-panel',
            data: {dialogTitle: title, dialogText: message}
        });
    }
}

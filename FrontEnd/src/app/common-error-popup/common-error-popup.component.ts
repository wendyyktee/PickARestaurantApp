import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

/**
 * This component allows other component to open dialog with customized title and content, dialog will close upon click on Close Button
 */
@Component({
  selector: 'app-common-error-popup',
  templateUrl: './common-error-popup.component.html',
  styleUrl: './common-error-popup.component.css'
})
export class CommonErrorPopupComponent {
  dialogTitle: string;
  dialogText: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any) {}

  ngOnInit() {
    this.dialogTitle = this.data.dialogTitle;
    this.dialogText = this.data.dialogText;
  }
}

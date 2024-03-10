import {Component, Inject} from '@angular/core';
import {Router} from "@angular/router";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

/**
 * This component allows other component to open dialog with customized title and content,
 * user will be redirect to home page upon click on Close Button
 */
@Component({
  selector: 'app-invalid-session-popup',
  templateUrl: './invalid-session-popup.component.html',
  styleUrl: './invalid-session-popup.component.css'
})
export class InvalidSessionPopupComponent {

  dialogTitle: string;
  dialogText: string;

  constructor(private router: Router, public dialogRef: MatDialogRef<InvalidSessionPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  closeDialog(){
    this.dialogRef.close();
    this.router.navigate(['/home'])
  }

  ngOnInit() {
    this.dialogTitle = this.data.dialogTitle;
    this.dialogText = this.data.dialogText;
  }

}

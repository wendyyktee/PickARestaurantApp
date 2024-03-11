import { Injectable } from '@angular/core';
import {Session} from "./session/session.model";

@Injectable()
export class SessionDataService {
  session: Session = {
    id: 0,
    // sessionCode: '',
    status: '',
    initiatorUserSessionId: '',
  };

  getSession(){
    return this.session;
  }

  setInitiatorUserSessionId(initiatorUserSessionId: string){
    this.session.initiatorUserSessionId = initiatorUserSessionId;
  }

  setId(sessionId: number){
    this.session.id = sessionId;
  }

  // setSessionCode(sessionCode: string){
  //   this.session.sessionCode = sessionCode;
  // }

  setStatus(status: string){
    this.session.status = status;
  }
}

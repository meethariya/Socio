import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Alert } from '../models/alert.model';

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  private alertSubject = new Subject<Alert>();

  getAlerts(): Observable<Alert> {
    return this.alertSubject.asObservable();
  }

  pushAlert(type: Alert['type'], message: Alert['message']) {
    this.alertSubject.next({
      id: new Date().getTime(),
      type: type,
      message: message,
    });
  }
}

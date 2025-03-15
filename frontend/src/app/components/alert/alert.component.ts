import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { AlertService } from '../../services/alert.service';
import { Subscription } from 'rxjs';
import { Alert } from '../../models/alert.model';

@Component({
  selector: 'app-alert',
  imports: [],
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.scss',
})
export class AlertComponent implements OnInit, OnDestroy {
  alerts = signal<Array<Alert>>([]);
  alertService = inject(AlertService);
  private subscription!: Subscription;
  readonly alertDuration = 4000;

  ngOnInit(): void {
    this.subscription = this.alertService.getAlerts().subscribe({
      next: (alert) => {
        this.alerts.update((values) => {
          return [...values, alert];
        });
        setTimeout(() => {
          this.alerts.update((values) => {
            return values.filter((a) => a.id !== alert.id);
          });
        }, this.alertDuration);
      },
    });
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}

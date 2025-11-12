import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RequestStatus } from '../../../core/models/approval-request.model';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span [class]="'status-badge status-' + status.toLowerCase()">
      {{ getStatusText() }}
    </span>
  `,
  styles: [`
    .status-badge {
      padding: 0.375rem 0.875rem;
      border-radius: 16px;
      font-size: 0.875rem;
      font-weight: 600;
      display: inline-block;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    
    .status-pending {
      background: #fef3c7;
      color: #92400e;
    }
    
    .status-approved {
      background: #d1fae5;
      color: #065f46;
    }
    
    .status-rejected {
      background: #fee2e2;
      color: #991b1b;
    }
  `]
})
export class StatusBadgeComponent {
  @Input() status!: RequestStatus;

  getStatusText(): string {
    switch (this.status) {
      case RequestStatus.PENDING:
        return 'Pendiente';
      case RequestStatus.APPROVED:
        return 'Aprobado';
      case RequestStatus.REJECTED:
        return 'Rechazado';
      default:
        return this.status;
    }
  }
}
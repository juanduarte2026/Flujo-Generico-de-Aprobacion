import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ApprovalService } from '../../../core/services/approval.service';
import { ApprovalRequest } from '../../../core/models/approval-request.model';
import { StatusBadgeComponent } from '../../../shared/components/status-badge/status-badge.component';

@Component({
  selector: 'app-pending-requests',
  standalone: true,
  imports: [CommonModule, RouterModule, StatusBadgeComponent],
  templateUrl: './pending-requests.component.html',
  styleUrls: ['./pending-requests.component.scss']
})
export class PendingRequestsComponent implements OnInit {
  requests: ApprovalRequest[] = [];
  loading = true;

  constructor(
    private authService: AuthService,
    private approvalService: ApprovalService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.approvalService.getPendingRequestsByApprover(user.username).subscribe({
        next: (data) => {
          this.requests = data;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
    }
  }

   // Calcula el tiempo transcurrido desde la creación de la solicitud
  getTimeElapsed(createdAt: Date): string {
    const now = new Date();
    const created = new Date(createdAt);
    const diffMs = now.getTime() - created.getTime();
    
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);

    if (diffDays > 0) {
      return `${diffDays} día${diffDays !== 1 ? 's' : ''}`;
    } else if (diffHours > 0) {
      return `${diffHours} hora${diffHours !== 1 ? 's' : ''}`;
    } else if (diffMins > 0) {
      return `${diffMins} minuto${diffMins !== 1 ? 's' : ''}`;
    } else {
      return 'Recién creada';
    }
  }
}
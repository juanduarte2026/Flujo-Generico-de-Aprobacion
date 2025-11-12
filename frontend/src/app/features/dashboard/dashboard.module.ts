import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { ApprovalService } from '../../core/services/approval.service';
import { User } from '../../core/models/user.model';
import { ApprovalRequest } from '../../core/models/approval-request.model';
import { StatusBadgeComponent } from '../../shared/components/status-badge/status-badge.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, StatusBadgeComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  currentUser: User | null = null;
  myRequests: ApprovalRequest[] = [];
  pendingApprovals: ApprovalRequest[] = [];
  loading = true;
  stats = {
    totalRequests: 0,
    pendingApprovals: 0,
    approved: 0,
    rejected: 0
  };

  constructor(
    private authService: AuthService,
    private approvalService: ApprovalService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser) {
      this.loadDashboardData();
    }
  }

  loadDashboardData(): void {
    if (!this.currentUser) return;

    this.loading = true;

    // Cargar mis solicitudes
    this.approvalService.getRequestsByRequester(this.currentUser.username)
      .subscribe({
        next: (requests) => {
          this.myRequests = requests.slice(0, 5); 
          this.stats.totalRequests = requests.length;
          this.stats.approved = requests.filter(r => r.status === 'APPROVED').length;
          this.stats.rejected = requests.filter(r => r.status === 'REJECTED').length;
        }
      });

    // Cargar solicitudes pendientes de aprobación
    this.approvalService.getPendingRequestsByApprover(this.currentUser.username)
      .subscribe({
        next: (requests) => {
          this.pendingApprovals = requests.slice(0, 5); 
          this.stats.pendingApprovals = requests.length;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
  }
}
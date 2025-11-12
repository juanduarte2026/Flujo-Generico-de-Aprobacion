import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ApprovalService } from '../../../core/services/approval.service';
import { ApprovalRequest } from '../../../core/models/approval-request.model';
import { StatusBadgeComponent } from '../../../shared/components/status-badge/status-badge.component';

@Component({
  selector: 'app-request-list',
  standalone: true,
  imports: [CommonModule, RouterModule, StatusBadgeComponent],
  templateUrl: './request-list.component.html',
  styleUrls: ['./request-list.component.scss']
})
export class RequestListComponent implements OnInit {
  requests: ApprovalRequest[] = [];
  loading = true;

  constructor(
    private authService: AuthService,
    private approvalService: ApprovalService
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.approvalService.getRequestsByRequester(user.username).subscribe({
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
}
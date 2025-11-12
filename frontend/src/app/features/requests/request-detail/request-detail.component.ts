import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { ApprovalService } from '../../../core/services/approval.service';
import { NotificationService } from '../../../core/services/notification.service';
import { ApprovalRequest } from '../../../core/models/approval-request.model';
import { User } from '../../../core/models/user.model';
import { StatusBadgeComponent } from '../../../shared/components/status-badge/status-badge.component';

@Component({
  selector: 'app-request-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, StatusBadgeComponent],
  templateUrl: './request-detail.component.html',
  styleUrls: ['./request-detail.component.scss']
})
export class RequestDetailComponent implements OnInit {
  request: ApprovalRequest | null = null;
  currentUser: User | null = null;
  loading = true;
  actionForm: FormGroup;
  processing = false;
  canApprove = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private approvalService: ApprovalService,
    private notificationService: NotificationService
  ) {
    this.actionForm = this.fb.group({
      comment: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadRequest(id);
    }
  }

  loadRequest(id: string): void {
    this.approvalService.getRequestById(id).subscribe({
      next: (data) => {
        this.request = data;
        this.canApprove = 
          data.status === 'PENDING' && 
          data.approver === this.currentUser?.username;
        this.loading = false;
      },
      error: () => {
        this.notificationService.showError('Solicitud no encontrada');
        this.router.navigate(['/dashboard']);
      }
    });
  }

  approveRequest(): void {
    if (this.actionForm.invalid || !this.request || !this.currentUser) {
      this.notificationService.showError('Por favor ingresa un comentario');
      return;
    }

    this.processing = true;
    const actionDTO = {
      performedBy: this.currentUser.username,
      comment: this.actionForm.value.comment
    };

    this.approvalService.approveRequest(this.request.id, actionDTO).subscribe({
      next: () => {
        this.notificationService.showSuccess('✅ Solicitud aprobada exitosamente');
        this.router.navigate(['/requests/pending']);
      },
      error: () => {
        this.processing = false;
      },
      complete: () => {
        this.processing = false;
      }
    });
  }

  rejectRequest(): void {
    if (this.actionForm.invalid || !this.request || !this.currentUser) {
      this.notificationService.showError('Por favor ingresa un comentario');
      return;
    }

    if (!confirm('¿Estás seguro de rechazar esta solicitud?')) {
      return;
    }

    this.processing = true;
    const actionDTO = {
      performedBy: this.currentUser.username,
      comment: this.actionForm.value.comment
    };

    this.approvalService.rejectRequest(this.request.id, actionDTO).subscribe({
      next: () => {
        this.notificationService.showSuccess('Solicitud rechazada');
        this.router.navigate(['/requests/pending']);
      },
      error: () => {
        this.processing = false;
      },
      complete: () => {
        this.processing = false;
      }
    });
  }

  get comment() { return this.actionForm.get('comment'); }
}
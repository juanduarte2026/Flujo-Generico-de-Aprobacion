import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ApprovalService } from '../../../core/services/approval.service';
import { NotificationService } from '../../../core/services/notification.service';
import { User } from '../../../core/models/user.model';
import { RequestType } from '../../../core/models/request-type.model';

@Component({
  selector: 'app-create-request',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-request.component.html',
  styleUrls: ['./create-request.component.scss']
})
export class CreateRequestComponent implements OnInit {
  requestForm: FormGroup;
  currentUser: User | null = null;
  users: User[] = [];
  requestTypes: RequestType[] = [];
  loading = false;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private approvalService: ApprovalService,
    private notificationService: NotificationService,
    private router: Router
  ) {
    this.requestForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      approver: ['', Validators.required],
      requestTypeId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.loadData();
  }

  loadData(): void {
    this.loading = true;

    // Cargar usuarios
    this.authService.getAllUsers().subscribe({
      next: (users) => {
        this.users = users.filter(u => u.username !== this.currentUser?.username);
      }
    });

    // Cargar tipos de solicitud
    this.approvalService.getAllRequestTypes().subscribe({
      next: (types) => {
        this.requestTypes = types;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.requestForm.invalid) {
      this.markFormGroupTouched(this.requestForm);
      this.notificationService.showError('Por favor completa todos los campos correctamente');
      return;
    }

    this.submitting = true;

    const requestData = {
      ...this.requestForm.value,
      requester: this.currentUser!.username,
      requestTypeId: Number(this.requestForm.value.requestTypeId)
    };

    this.approvalService.createRequest(requestData).subscribe({
      next: (response) => {
        this.notificationService.showSuccess('✅ Solicitud creada exitosamente');
        this.router.navigate(['/requests/detail', response.id]);
      },
      error: () => {
        this.submitting = false;
      },
      complete: () => {
        this.submitting = false;
      }
    });
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(key => {
      formGroup.get(key)?.markAsTouched();
    });
  }

  get title() { return this.requestForm.get('title'); }
  get description() { return this.requestForm.get('description'); }
  get approver() { return this.requestForm.get('approver'); }
  get requestTypeId() { return this.requestForm.get('requestTypeId'); }
}
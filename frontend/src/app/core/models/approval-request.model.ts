import { RequestType } from './request-type.model';

export enum RequestStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

export interface ApprovalRequest {
  id: string;
  title: string;
  description: string;
  requester: string;
  approver: string;
  requestType: RequestType;
  status: RequestStatus;
  createdAt: Date;
  updatedAt: Date;
  requesterFullName?: string;
  approverFullName?: string;
}
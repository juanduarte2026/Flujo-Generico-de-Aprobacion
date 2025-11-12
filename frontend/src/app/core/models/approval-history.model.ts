export interface ApprovalHistory {
  id: number;
  requestId: string;
  action: string;
  performedBy: string;
  comment: string;
  timestamp: Date;
  performedByFullName?: string;
}
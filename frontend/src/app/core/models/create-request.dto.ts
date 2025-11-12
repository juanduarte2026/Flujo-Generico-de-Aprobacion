export interface CreateRequestDTO {
  title: string;
  description: string;
  approver: string;  
  requester: string;
  requestTypeId: number;
}
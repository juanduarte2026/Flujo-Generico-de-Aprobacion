import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApprovalRequest, RequestStatus } from '../models/approval-request.model';
import { RequestType } from '../models/request-type.model';
import { CreateRequestDTO } from '../models/create-request.dto';
import { ApprovalActionDTO } from '../models/approval-action.dto';

@Injectable({
  providedIn: 'root'
})
export class ApprovalService {
  private apiUrl = `${environment.apiUrl}/requests`;

  constructor(private http: HttpClient) {}

  // POST /api/requests - Crear nueva solicitud
  createRequest(dto: CreateRequestDTO): Observable<ApprovalRequest> {
    return this.http.post<ApprovalRequest>(this.apiUrl, dto);
  }

  // GET /api/requests - Obtener todas las solicitudes
  getAllRequests(): Observable<ApprovalRequest[]> {
    return this.http.get<ApprovalRequest[]>(this.apiUrl);
  }

   // GET /api/requests/{id} - Obtener solicitud por ID
  getRequestById(id: string): Observable<ApprovalRequest> {
    return this.http.get<ApprovalRequest>(`${this.apiUrl}/${id}`);
  }

   // GET /api/requests/requester/{username} - Solicitudes del usuario
  getRequestsByRequester(username: string): Observable<ApprovalRequest[]> {
    return this.http.get<ApprovalRequest[]>(`${this.apiUrl}/requester/${username}`);
  }

   // GET /api/requests/approver/{username}/pending - Solicitudes pendientes del aprobador
  getPendingRequestsByApprover(username: string): Observable<ApprovalRequest[]> {
    return this.http.get<ApprovalRequest[]>(`${this.apiUrl}/approver/${username}/pending`);
  }

   // GET /api/requests/status/{status} - Solicitudes por estado
  getRequestsByStatus(status: RequestStatus): Observable<ApprovalRequest[]> {
    return this.http.get<ApprovalRequest[]>(`${this.apiUrl}/status/${status}`);
  }

  //  PUT /api/requests/{id}/approve - Aprobar solicitud
  approveRequest(id: string, actionDTO: ApprovalActionDTO): Observable<ApprovalRequest> {
    return this.http.put<ApprovalRequest>(`${this.apiUrl}/${id}/approve`, actionDTO);
  }

  //  PUT /api/requests/{id}/reject - Rechazar solicitud
  rejectRequest(id: string, actionDTO: ApprovalActionDTO): Observable<ApprovalRequest> {
    return this.http.put<ApprovalRequest>(`${this.apiUrl}/${id}/reject`, actionDTO);
  }

   // GET /api/requests/types - Obtener tipos de solicitud
  getAllRequestTypes(): Observable<RequestType[]> {
    return this.http.get<RequestType[]>(`${this.apiUrl}/types`);
  }

   // GET /api/requests/approver/{username}/pending/count - Contar pendientes
  countPendingRequests(username: string): Observable<{ count: number }> {
    return this.http.get<{ count: number }>(`${this.apiUrl}/approver/${username}/pending/count`);
  }
}
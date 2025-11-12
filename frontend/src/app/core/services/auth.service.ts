import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from '../models/user.model';
import { LoginDTO } from '../models/login.dto';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient) {}


   // Login - Autentica usuario y guarda en localStorage
  login(credentials: LoginDTO): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, credentials).pipe(
      tap(user => {
        this.setCurrentUser(user);
      })
    );
  }

   // Logout - Elimina sesión
  logout(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('currentUser'); 
    }
    this.currentUserSubject.next(null);
  }

   // Obtiene el usuario actual
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }


   // Verifica si hay usuario autenticado
  isAuthenticated(): boolean {
    return this.currentUserSubject.value !== null;
  }


   // Obtiene todos los usuarios activos (para selectores)
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/users`);
  }


   // Obtiene usuario por username
  getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/users/${username}`);
  }


   // Guarda usuario en localStorage y BehaviorSubject
  private setCurrentUser(user: User): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem('currentUser', JSON.stringify(user)); 
    }
    this.currentUserSubject.next(user);
  }


   // Recupera usuario de localStorage (solo si estamos en navegador)
  private getUserFromStorage(): User | null {
    if (typeof window === 'undefined') return null; 
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }
}

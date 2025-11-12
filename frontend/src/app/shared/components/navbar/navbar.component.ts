import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ApprovalService } from '../../../core/services/approval.service';
import { User } from '../../../core/models/user.model';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  currentUser: User | null = null;
  pendingCount: number = 0;
  mobileMenuOpen: boolean = false;

  constructor(
    private authService: AuthService,
    private approvalService: ApprovalService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Suscribirse a cambios del usuario actual
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.loadPendingCount();
        // Actualizar contador cada 30 segundos
        setInterval(() => this.loadPendingCount(), 30000);
      }
    });
  }

  /**
   * Carga el contador de solicitudes pendientes
   */
  loadPendingCount(): void {
    if (this.currentUser) {
      this.approvalService.countPendingRequests(this.currentUser.username)
        .subscribe({
          next: (response) => {
            this.pendingCount = response.count;
          },
          error: () => {
            this.pendingCount = 0;
          }
        });
    }
  }

  /**
   * Obtiene las iniciales del nombre completo del usuario
   * Ejemplo: "Juan Pérez" → "JP"
   */
  getInitials(fullName: string): string {
    if (!fullName) return '??';
    
    const names = fullName.trim().split(' ');
    if (names.length === 1) {
      return names[0].substring(0, 2).toUpperCase();
    }
    
    return (names[0][0] + names[names.length - 1][0]).toUpperCase();
  }

  /**
   * Cierra la sesión del usuario
   */
  logout(): void {
    this.authService.logout();
    this.closeMobileMenu();
    this.router.navigate(['/login']);
  }

  /**
   * Alterna el menú móvil
   */
  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  /**
   * Cierra el menú móvil
   */
  closeMobileMenu(): void {
    this.mobileMenuOpen = false;
  }
}
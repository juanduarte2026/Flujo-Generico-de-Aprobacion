import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

   // Muestra notificación de éxito
  showSuccess(message: string): void {
    this.showNotification(message, 'success');
  }

   // Muestra notificación de error
  showError(message: string): void {
    this.showNotification(message, 'error');
  }

   // Muestra notificación de información
  showInfo(message: string): void {
    this.showNotification(message, 'info');
  }

   // Sistema simple de notificaciones 
  private showNotification(message: string, type: 'success' | 'error' | 'info'): void {
    
    // Crear elemento de notificación
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.textContent = message;
    
    Object.assign(notification.style, {
      position: 'fixed',
      top: '20px',
      right: '20px',
      padding: '16px 24px',
      borderRadius: '8px',
      color: 'white',
      fontWeight: '500',
      zIndex: '9999',
      animation: 'slideIn 0.3s ease-out',
      boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
      maxWidth: '400px',
      backgroundColor: this.getBackgroundColor(type)
    });

    // Agregar al DOM
    document.body.appendChild(notification);

    // Remover después de 4 segundos
    setTimeout(() => {
      notification.style.animation = 'slideOut 0.3s ease-in';
      setTimeout(() => notification.remove(), 300);
    }, 4000);
  }

  private getBackgroundColor(type: string): string {
    switch (type) {
      case 'success': return '#10b981';
      case 'error': return '#ef4444';
      case 'info': return '#3b82f6';
      default: return '#6b7280';
    }
  }
}
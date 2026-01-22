import { CommonModule, formatDate } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { AccordionModule } from 'primeng/accordion';
import { CalendarModule } from 'primeng/calendar';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { Table, TableModule } from 'primeng/table';
import { OrderHistoryResponse } from '../../../models/order-history.model';
import { OrderService } from '../../../services/order.service';
import { EmployeeService } from '../../../services/employee.service';

@Component({
  selector: 'app-order-history',
  standalone: true,
  imports: [AccordionModule, TableModule, ProgressSpinnerModule, HttpClientModule, CommonModule, TranslateModule, CalendarModule, FormsModule],
  templateUrl: './order-history.component.html',
  styleUrl: './order-history.component.scss',
  providers: [OrderService, EmployeeService]
})
export class OrderHistoryComponent {

  today: Date = new Date();
  orderHistory: OrderHistoryResponse[] = [];
  employees: any[] = [];
  currentEmployeeId: string = '';
  exportData: any[] = [];


  loading = true;

  startDate: Date | null = null;
  endDate: Date | null = null;

  constructor(private readonly orderService: OrderService, private employeeService: EmployeeService) { }

  ngOnInit(): void {
 const userData = localStorage.getItem('user');
  if (userData) {
    const parsed = JSON.parse(userData);
    const userId = parsed.userId;

    this.employeeService.getEmployeeIdByUserId(userId).subscribe({
      next: (employeeId) => {
        this.currentEmployeeId = employeeId.toString();
        this.filterOrdersByDate()
      },
      error: (err) => console.error('Error fetching employee ID', err)
    });
  }

  const savedStart = localStorage.getItem('dashboardStartDate');
  const savedEnd = localStorage.getItem('dashboardEndDate');

  this.startDate = savedStart ? new Date(savedStart) : this.today;
  this.endDate = savedEnd ? new Date(savedEnd) : this.today;

  
  }

  getEmployeeId() {
    this.employeeService.getAll().subscribe({
      next: (data) => {
        this.employees = data;
        this.loading = false
      },
      error: (error) => {
        console.log("Error fetching employees", error);
        this.loading = false;
      }
    })


  }

  filterOrdersByDate() {
    if (this.startDate && this.endDate && this.currentEmployeeId) {
      const start = formatDate(this.startDate, 'yyyy-MM-dd', 'en-IN');
      const end = formatDate(this.endDate, 'yyyy-MM-dd', 'en-IN')

      localStorage.setItem('dashboardStartDate',start)
      localStorage.setItem('dashboardEndDate',end)

      this.orderService.getOrdersOfCurrentEmployee(this.currentEmployeeId,start, end).subscribe({
        next: (data) => {
          this.orderHistory = data;
          this.loading = false;
        },

        error: (error) => {
          console.log("Error Fetching order details", error)
          this.loading = false;
        }
      })
    }
  }

  

  downloadInvoice(orderId: string) {
  this.orderService.downloadInvoice(orderId).subscribe({
    next: (data) => {
      const blob = new Blob([data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `Invoice-${orderId}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    },
    error: (err) => {
      console.error('Error downloading invoice', err);
    }
  });
}



}

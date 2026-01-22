import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../../services/order.service';
import { AccordionModule } from 'primeng/accordion';
import { TableModule } from 'primeng/table';
import { ProgressSpinnerModule } from 'primeng/progressspinner'
import { HttpClientModule } from '@angular/common/http';
import { OrderHistoryResponse } from '../../../models/order-history.model';
import { CommonModule, formatDate } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { EmployeeService } from '../../../services/employee.service';
import { CalendarModule } from "primeng/calendar";
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [AccordionModule, TableModule, ProgressSpinnerModule, HttpClientModule, CommonModule, TranslateModule, CalendarModule, FormsModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.scss',
  providers: [OrderService, EmployeeService]
})
export class ReportsComponent implements OnInit {
  today:Date = new Date();
  orderHistory: OrderHistoryResponse[] = [];
  loading = true;
  employees: any[] = []

  startDate : Date | null = null;
  endDate : Date | null = null;

  constructor(private readonly orderService: OrderService, private readonly employeeService: EmployeeService) { }

  ngOnInit(): void {

    const savedStart = localStorage.getItem('dashboardStartDate')
    const savedEnd = localStorage.getItem('dashboardEndDate')

    this.startDate = savedStart? new Date(savedStart) :this.today;
    this.endDate = savedEnd? new Date(savedEnd):  this.today;

    this.filterOrdersByDate();


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
    if (this.startDate && this.endDate) {
      const start = formatDate(this.startDate, 'yyyy-MM-dd', 'en-IN');
      const end = formatDate(this.endDate, 'yyyy-MM-dd', 'en-IN')

      localStorage.setItem('dashboardStartDate',start)
      localStorage.setItem('dashboardEndDate',end)

      this.orderService.getOrderHistoryByDateRange(start, end).subscribe({
        next: (data) => {
          this.orderHistory = data;
          this.loading = false

        },
        error: (error) => {
          console.log("Error Fetching order details", error)
          this.loading = false;
        }
      })
    }
  }

}

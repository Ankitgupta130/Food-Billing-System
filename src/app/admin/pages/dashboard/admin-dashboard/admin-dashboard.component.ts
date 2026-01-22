import { CommonModule, formatDate } from '@angular/common';
import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ChartModule } from 'primeng/chart';
import { TableModule } from 'primeng/table'
import { EmployeeService } from '../../../../services/employee.service';
import { OrderService } from '../../../../services/order.service';
import { TranslateModule } from '@ngx-translate/core';
import { CalendarModule } from "primeng/calendar";
import { FormsModule } from '@angular/forms';
import { plugins, Tooltip } from 'chart.js';
import { callback } from 'chart.js/dist/helpers/helpers.core';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, CardModule, ChartModule, TableModule, TranslateModule, CalendarModule,FormsModule, CalendarModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss',
  providers: [EmployeeService, OrderService]
})
export class AdminDashboardComponent {
  today:Date = new Date();

  topSellingChartData: any;
  topSellingChartOptions: any;
  emp: any[] = [];
  empLength!: number;

  order: any[] = [];
  orderLength!: number;

  recentOrder: any[] = [];
  todayRevenue!: number;
  totalRevenue!: number;

  startDate: Date | null = null;
  endDate: Date | null = null;

  constructor(private employeeService: EmployeeService, private orderService: OrderService) { }

  ngOnInit() {
    this.getEmployees();
    this.getTotalOrders();

    const savedStart = localStorage.getItem('dashboardStartDate')
    const savedEnd = localStorage.getItem('dashboardEndDate')
    
    this.startDate = savedStart? new Date(savedStart):this.today;
    this.endDate = savedEnd? new Date(savedEnd) :this.today;

    this.filterOrdersByDate();
  }

  getEmployees() {
    this.employeeService.
      getAll()
      .subscribe((data) => {
        (this.emp) = data;
        this.empLength = this.emp.length;

      })
  }

  getTotalOrders() {
    this.orderService.getOrderHistory()
      .subscribe((data) => {
        (this.order) = data;

        this.totalRevenue = this.order.reduce((sum, order) => sum + (order.totalAmount || 0), 0)

      })
  }

  filterOrdersByDate() {
    if(this.startDate && this.endDate) {
      const start = formatDate(this.startDate, 'yyyy-MM-dd', 'en-IN')
      const end= formatDate(this.endDate,'yyyy-MM-dd', 'en-IN')

      localStorage.setItem('dashboardStartDate', this.startDate.toISOString())
      localStorage.setItem('dashboardEndDate',this.endDate.toISOString())

      this.orderService.getOrderHistoryByDateRange(start, end).subscribe({
        next:(data)=>{
          this.order = data
          this.todayRevenue = this.order.reduce((sum,data)=> sum +(data.totalAmount || 0),0)
          this.orderLength = this.order.length;
          this.recentOrder = this.order;
          

      const itemMap: { [key: string]: number } = {};

      data.forEach(order => {
        (order.items as any[]).forEach(item => {
          if (itemMap[item.foodItemName]) {
            itemMap[item.foodItemName] += item.quantity;
          } else {
            itemMap[item.foodItemName] = item.quantity;
          }
        });
      });

      const sortedItems = Object.entries(itemMap)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 5);

      const labels = sortedItems.map(([name]) => name);
      const data1 = sortedItems.map(([, qty]) => qty);

      const total = data1.reduce((a, b) => a + b, 0);
      const dataPercent = data1.map(value => parseFloat(((value / total) * 100).toFixed(0)));
      

      this.topSellingChartData = {
        labels,
        datasets: [
          {
            data: dataPercent,
            backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#66BB6A', '#9575CD'],
            hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#66BB6A', '#9575CD']
          }
        ]
      };
        },
        error:(error)=>{
          console.error('Failed to search order:',error);
        }
      })

      this.topSellingChartOptions = {
        plugins: {
          tooltip:{
            callbacks:{
              label: function (context: any){
                const label = context.label || '';
                const value = context.raw;
                return `${label}: ${value}%`
              }
            }
          },
          legend: {
            position: 'top'
          }
        },
        responsive: true,
        maintainAspectRatio: false
      }
    }
  }

}

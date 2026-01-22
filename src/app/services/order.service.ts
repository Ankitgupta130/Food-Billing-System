import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { OrderHistoryResponse } from '../models/order-history.model';

interface OrderRequest {
  customerName: string;
  mobileNumber: string;
  employeeId: number;
  totalAmount: number;
  cartItems: {
    foodItemId: number;
    quantity: number;
  }[];
}

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private baseUrl = 'http://localhost:8080/api/orders'
  private invoiceUrl = 'http://localhost:8080/api'

  constructor(private http: HttpClient) { }

  placeOrder(payload: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/place-order`, payload, { responseType: 'text' }).pipe(
      catchError(e => {
        return throwError(() => e);
      })
    );
  }

  getOrderHistory(): Observable<OrderHistoryResponse[]> {
    return this.http.get<OrderHistoryResponse[]>(`${this.baseUrl}/history`)

  }

  getOrderHistoryByDateRange(startDate: string, endDate: string): Observable<OrderHistoryResponse []> {
    return this.http.get<OrderHistoryResponse[]> (`${this.baseUrl}/filter?startDate=${startDate}&endDate=${endDate}`).pipe(
      catchError(e =>{
        
        return throwError(() => e)
      })
    )
  }

  getOrdersOfCurrentEmployee(employeeId: string, startDate: string, endDate: string): Observable<OrderHistoryResponse []> {
    return this.http.get<OrderHistoryResponse[]> (`${this.baseUrl}/employee?employeeId=${employeeId}&startDate=${startDate}&endDate=${endDate}`).pipe(
      catchError(e =>{
        return throwError(()=>e)
      })
    );
  }

  downloadInvoice(orderId: string) {
    return this.http.get(`${this.invoiceUrl}/invoices/${orderId}`,{responseType: 'blob'});
  }
}

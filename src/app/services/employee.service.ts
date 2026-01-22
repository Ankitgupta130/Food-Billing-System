import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, retry } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private baseurl = "http://localhost:8080/api/employees";

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<any[]>(this.baseurl)
  }

  create(employee:any) {
    return this.http.post(this.baseurl,{
      ...employee,
      adminId:3,
    })
  }


  update(id: number, employee: any) {
    return this.http.put(`${this.baseurl}/${id}`,employee)
  }

  delete(id: number) {
    return this.http.delete(`${this.baseurl}/${id}`)
  }

  checkUsername(username: string) {
    return this.http.get<boolean>(`${this.baseurl}/check-username?username=${username}`)
  }

  getEmployeeIdByUserId(userId: number): Observable<number> {
    return this.http.get<number>(`${this.baseurl}/by-user/${userId}`)
  }
}

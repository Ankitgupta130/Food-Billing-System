import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface FoodItem {
  id? : number;
  name: string;
  price: number;
  imageUrl ?: string;

}

@Injectable({
  providedIn: 'root'
})
export class FoodItemService {

  public baseUrl = "http://localhost:8080/api/foods";
  public imageBaseUrl: string =  "http://localhost:8080";

  constructor(private http:HttpClient) { }

  getAll(): Observable<FoodItem[]> {
    return this.http.get<FoodItem[]>(this.baseUrl);
  }

  add(foodItem: FoodItem): Observable<any> {
    return this.http.post(this.baseUrl, foodItem)
  }

  update(id:number, foodItem: FoodItem) : Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, foodItem)
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`)
  }

  uploadImage(formData: FormData): Observable<any> {
  return this.http.post<{ filename: string }>("http://localhost:8080/api/foods/upload", formData);
}

addMultipart(data: FormData) {
  return this.http.post<any>(`${this.baseUrl}`, data);
}

updateMultipart(id: number, data: FormData) {
  return this.http.put<any>(`http://localhost:8080/upload/${id}`, data);
}


}

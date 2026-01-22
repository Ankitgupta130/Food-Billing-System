import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TranslationService {
  private baseUrl = "http://localhost:8080/api/i18n"

  constructor(private http:HttpClient) { }

  getTranslation(lang:String): Observable<any> {
    return this.http.get(`${this.baseUrl}?lang=${lang}`)
  }
}

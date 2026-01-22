import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { PanelMenuModule } from 'primeng/panelmenu';
import { ButtonModule } from 'primeng/button';
import { TranslateModule,TranslateService } from '@ngx-translate/core';
import { MenuItem } from 'primeng/api';
import { DropdownModule } from "primeng/dropdown";
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, MenubarModule, PanelMenuModule, ButtonModule, TranslateModule, DropdownModule, FormsModule, DropdownModule],
  templateUrl: './admin-layout.component.html',
  styleUrl: './admin-layout.component.scss',
})
export class AdminLayoutComponent {
  selectedLanguage ={label: 'English', value:'en'};

  items: MenuItem[] = [];

  languages = [
    {label:'English', value:'en'},
    {label:'Hindi',value:'hi'},
    {label:'Marathi', value:'ma'},
    {label:'Japanese', value:'jap'}
  ]

    constructor(private translate: TranslateService, private router : Router) {
    this.translate.setDefaultLang('en');
    this.translate.use('en')
  }

  ngOnInit(){
    this.setItems();
  }

  setItems() {

    this.translate.get([
      'ADMIN.DASHBOARD',
      'ADMIN.EMPLOYEES',
      'ADMIN.FOOD_MENU',
      'ADMIN.REPORTS',
      'ADMIN.LOGOUT'

    ]).subscribe((res: {[key: string]: string}) =>{
       this.items = [
    {
      label : res['ADMIN.DASHBOARD'],
      icon: 'pi pi-home',
      routerLink: ['dashboard']
    },

    {
      label: res['ADMIN.EMPLOYEES'],
      icon: 'pi pi-users',
      routerLink: ['employees']
    },

    {
      label: res['ADMIN.FOOD_MENU'],
      icon: 'pi pi-shopping-cart',
      routerLink: ['food-item']
    },

    {
      label: res['ADMIN.REPORTS'],
      icon: 'pi pi-chart-bar',
      routerLink: ['reports']
    },

    {
      label: res['ADMIN.LOGOUT'],
      icon: 'pi pi-sign-out',
      command: () =>{
        localStorage.clear();
        this.router.navigate(['/login']);
      }
    }

  ]

  }
   ) }
    
 
  
changeLanguage(lang: any) {
  this.translate.use(lang.value).subscribe(()=>{
    this.setItems();
  });
}

}


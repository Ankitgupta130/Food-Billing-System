import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { MenubarModule } from 'primeng/menubar';
import { PanelMenuModule } from 'primeng/panelmenu';

@Component({
  selector: 'app-employee-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, ButtonModule, TranslateModule, DropdownModule, FormsModule],
  templateUrl: './employee-layout.component.html',
  styleUrl: './employee-layout.component.scss'
})
export class EmployeeLayoutComponent {
  selectedLanguage= {label:'English', value:'en'};
  languages = [
    { label:'English', value:'en'},
    {label:'Hindi', value:'hi'},
    {label:'Marathi', value:'ma'},
    {label:'Japanese',value:'jap'}
  ]

  constructor(private router: Router, private translate: TranslateService) {
    this.translate.setDefaultLang('en');
    this.translate.use('en')
  }

  changeLanguage(lang: any) {
    this.translate.use(lang.value);
    this.selectedLanguage = lang;
  }

  goToOrderHistory(){
    this.router.navigate(['/employee/orderhistory'])
  }


  logOut() {
    this.router.navigate(['/login'])
    localStorage.clear();
  }
  

}

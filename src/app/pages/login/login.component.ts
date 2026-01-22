import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/login.model';
import { HttpClientModule } from '@angular/common/http';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { TranslateModule,TranslateService } from '@ngx-translate/core';
import { DropdownModule } from "primeng/dropdown";
import { NgxCaptchaModule } from 'ngx-captcha';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    ReactiveFormsModule,
    RippleModule,
    HttpClientModule,
    ToastModule,
    TranslateModule,
    DropdownModule,
    FormsModule,
    NgxCaptchaModule
],
  providers: [AuthService, MessageService],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm!: FormGroup;
  submitted = false;

  captchaFlag=false;
  // captcha: string ='';
  selectedLanguage = {label:'English', value:'en'};

  token: string | null = null;
  siteKey =this.authService.siteKey;
  
  languages = [
    {label: 'English', value:'en'},
    {label: 'Hindi', value:'hi'},
    {label: 'Marathi', value:'ma'},
    {label: 'Japanese', value:'jap'}
  ]

  

  // generateCaptcha(length: number = 6): string {
  //   const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  //   let captcha = '';
  //   for(let i=0;i<length;i++) {
  //     captcha += chars.charAt(Math.floor(Math.random() * chars.length));
  //   }
  //   return captcha;
  // }

  // refreshCaptcha() {
  //   this.captcha = this.generateCaptcha();
  // }

  // validateCaptcha() {
  //   return this.captcha === this.loginForm.get('captchaInput')?.value;
  // }


  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private messageService: MessageService,
    private translate: TranslateService
  ) {
    this.translate.setDefaultLang('en');
    this.translate.use('en')
   }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: new FormControl<string>('', [Validators.required]),
      password: new FormControl<string>('', [Validators.required, Validators.minLength(6)]),
      captchaInput:['',Validators.required]
    });
    // this.refreshCaptcha();
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.loginForm.valid) {
         const loginPayload: LoginRequest = this.loginForm.getRawValue();
 
    this.authService.login(loginPayload).subscribe({
      next: (res) => {
        this.authService.saveUserData(res);
          // if (res.role === 'ADMIN') {
          //   this.router.navigate(['/admin/dashboard']);
          // } else {
          //   this.router.navigate(['/employee']);
          // }

          if(res.status=="ACTIVE"){
              if (res.role === 'ADMIN') {
            this.router.navigate(['/admin/dashboard']);
          } else {
            this.router.navigate(['/employee']);
          }
          }else{
             this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('LOGIN.LOGIN_FAILED'),
          detail: this.translate.instant('LOGIN.deactive'),
          life: 3000
        });
          }
        
      },
      error: (err) => {

        console.log(err,"log");

         this.messageService.add({
          severity: 'error',
          summary: this.translate.instant('LOGIN.LOGIN_FAILED'),
          detail: this.translate.instant('LOGIN.LOGIN_FAILED_DETAIL'),
          life: 3000
        });
        
       
      }
    });
    }

    // if(!this.validateCaptcha()) {
    //   this.messageService.add({
    //     severity: 'error',
    //     summary: this.translate.instant('LOGIN.CAPTCHA_FAILED'),
    //     detail: this.translate.instant('LOGIN.INVALID_CAPTCHA'),
    //     life: 3000
    //   });
    //   this.refreshCaptcha();
    //   this.loginForm.get('captchaInput')?.reset();   
    //   return;
    // }

 
  }

  changeLanguage(lang: any) {
    this.translate.use(lang.value);
  }


  handleSuccess(token: string): void {
    this.token = token;
    console.log('Token generated:', token);
 
    this.authService.reCaptcha(token).subscribe({
      next: (res: any) => {
        if (res.success) {
          console.log("taken has been completely success");
         
          console.log('CAPTCHA verified by backend');
        } else {
          console.warn('CAPTCHA verification failed');
        }
      },
      error: err => console.error('CAPTCHA verification error:', err)
 
    });
 
  }
 
  handleLoad(): void {
    console.log('Captcha loaded');
  }
 
  handleReset(): void {
    console.log('Captcha reset');
  }
 
  handleExpire(): void {
    console.log('Captcha expired');
  }
}

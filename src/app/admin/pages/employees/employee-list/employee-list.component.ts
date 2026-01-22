import { Component } from '@angular/core';
import { EmployeeService } from '../../../../services/employee.service';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import {
  AbstractControl,
  AsyncValidatorFn,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, map } from 'rxjs/operators';
import { TranslateModule } from '@ngx-translate/core';
import { StatusResponse } from '../../../../models/login.model';
import { AuthService } from '../../../../services/auth.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    TranslateModule,
    ToastModule
  ],
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.scss',
  providers: [EmployeeService, AuthService, MessageService]
})
export class EmployeeListComponent {
  employees: any[] = [];
  showDialog = false;
  isEdit = false;
  showPassword = false;
  showConfirmPassword = false;
  employeeForm!: FormGroup;
  actions = false;

  payload!: StatusResponse;


  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private authService: AuthService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.getEmployees();
    this.initForm();
  }

  initForm() {
    this.employeeForm = this.fb.group(
      {
        name: [
          '',
          [Validators.required, Validators.pattern(/^(?!\s*$)[A-Za-z ]{3,}$/)]
        ],
        phone: [
          '',
          [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]
        ],
        email: [
          '',
          [
            Validators.required,
            Validators.pattern(
              /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
            )
          ]
        ],
        username: [
          '',
          [Validators.required, Validators.pattern(/^[a-zA-Z0-9_]{4,20}$/)],
          [this.usernameExistsValidator()]
        ],
        password: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]]
      },
      { validators: this.passwordMatchValidator }
    );
  }

  passwordMatchValidator: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    const password = group.get('password')?.value;
    const confirm = group.get('confirmPassword')?.value;
    return password === confirm ? null : { mismatch: true };
  };

  usernameExistsValidator(): AsyncValidatorFn {
    return (control: AbstractControl) => {
      if (!control.value) return of(null);

      return this.employeeService.checkUsername(control.value).pipe(
        map((exists) => (exists ? { usernameTaken: true } : null)),
        catchError(() => of(null))
      );
    };
  }

  openDialog(employee?: any) {
    this.isEdit = !!employee;
    this.showPassword = false;
    this.showConfirmPassword = false;

    if (this.isEdit) {
      this.employeeForm = this.fb.group({
        id: [employee.id],
        name: [
          employee.name,
          [Validators.required, Validators.pattern(/^[A-Za-z ]{3,}$/)]
        ],
        phone: [
          employee.phone,
          [Validators.required, Validators.pattern(/^[6-9]\d{9}$/)]
        ],
        email: [employee.email, [Validators.required, Validators.email]],
        username: [{ value: employee.username, disabled: true }],
        password: [''],
        confirmPassword: ['']
      });
    } else {
      this.initForm();
    }

    this.showDialog = true;
  }

  getEmployees() {
    this.employeeService
      .getAll()
      .subscribe((data) => (this.employees = data));
  }

  disablePaste(event: ClipboardEvent) {
    event.preventDefault();
  }

  allowOnlyLetters(event: KeyboardEvent) {
    const inputChar = event.key;
    if (!/^[a-zA-Z\s]*$/.test(inputChar)) {
      event.preventDefault();
    }
  }

  allowOnlyDigits(event: KeyboardEvent) {
    const inputChar = event.key;
    if (!/^\d$/.test(inputChar)) {
      event.preventDefault();
    }
  }

  formatName(name: String) {
    return name
      .trim()
      .replace(/\s+/g, ' ')
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }

  saveEmployee() {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }

    const data = this.employeeForm.getRawValue();

    const formattedName = this.formatName(data.name);

    if (this.isEdit) {
      const updated = {
        name: formattedName,
        phone: data.phone.trim(),
        email: data.email.trim()
      };

      this.employeeService.update(data.id, updated).subscribe(() => {
        this.getEmployees();
        this.showDialog = false;
      });
    } else {
      const newEmployee = {
        ...data,
        name: formattedName,
        phone: data.phone.trim(),
        email: data.email.trim(),
        username: data.username.trim(),
        password: data.password,
        adminId: 3
      }

      this.employeeService.create(newEmployee).subscribe(() => {
        this.getEmployees();
        this.showDialog = false;
      });
    }
  }

  deleteEmployee(id: number) {
    if (confirm('Are you sure you want to delete?')) {
      this.employeeService.delete(id).subscribe(() => this.getEmployees());
    }
  }

  setStatus(id1: number, actions: string,name:string) {

    console.log(id1);
    


    if (actions == "INACTIVE") {

 this.payload={
  id: id1,
  action: actions
};



      this.authService.setUserStatus(this.payload).subscribe({
        next: (res) => {
        confirm("Are you sure you want to deactivate user?")
        this.messageService.add({
        severity:'success',
        summary: 'User Deactivated Successfully',
        detail: `${name} Deactivated Successfully `,
        life: 1000
    })
          console.log("res", res);
             this.getEmployees();

        },
        error: (err) => {
          console.log(err);

        }
      })
    } else {

       this.payload={
  id: id1,
  action: "ACTIVE"
};


      this.authService.setUserStatus(this.payload).subscribe({
        next: (res) => {
          confirm("Are you sure you want to activate user?")
          this.messageService.add({
            severity:'success',
            summary:'User Activated Successfully',
            detail:`${name} Activated Successfully`
          })
          console.log("res", res);
             this.getEmployees();


        },
        error: (err) => {
          console.log(err);

        }
      })




     


    }
  }
}

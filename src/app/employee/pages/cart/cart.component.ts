import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AuthService } from '../../../services/auth.service';
import { OrderService } from '../../../services/order.service';
import { CartService } from '../../../services/cart.service';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { EmployeeService } from '../../../services/employee.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    HttpClientModule,
    InputTextModule,
    ButtonModule,
    RouterModule,
    TranslateModule
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
  providers: [OrderService,AuthService,EmployeeService]
})
export class CartComponent {
  cart = this.cartService.cartItems;
  totalAmount = 0.0;
  customerForm!: FormGroup;
  employeeId !: number;

  constructor(
    private cartService: CartService,
    private authService: AuthService,
    private orderService: OrderService,
    private fb: FormBuilder,
    private router: Router,
    private employeeService: EmployeeService
  ) {}

  ngOnInit() {
    this.customerForm = this.fb.group({
      customerName: ['', Validators.required],
      mobileNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]]
    });

    this.calculateTotal();

    this.cart().forEach(item => {
      if (item.quantity <= 0) {
        this.updateQuantity(item.id, 1);
      }
    });

    const user = this.authService.getUserData();
    if(user) {
      this.employeeService.getEmployeeIdByUserId(user.userId).subscribe({
        next:(data)=>{
          this.employeeId = data;
        },
        error:(error)=>{
          console.log('Failed to fetch employeed details',error);
        }
      })
    }
  }

  calculateTotal() {
    this.totalAmount = this.cart().reduce(
      (acc, item) => acc + item.price * item.quantity,
      0
    );
  }

  updateQuantity(itemId: number, quantity: number) {
    this.cartService.updateQuantity(itemId, quantity);
    this.calculateTotal();
  }

  removeItem(itemId: number) {
    this.cartService.removeItem(itemId);
    this.calculateTotal();
  }

  clearCart() {
    this.cartService.clearCart();
    this.totalAmount = 0;
  }

  placeOrder() {
    const user = this.authService.getUserData();
    if (!user) {
      alert('Please login first');
      this.router.navigate(['/login']);
      return;
    }

    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      alert('Please enter valid customer details.');
      return;
    }

    if(!this.employeeId){
      alert('Employee Details not found. Please try again later.')
    }

    const { customerName, mobileNumber } = this.customerForm.value;

    const orderPayload = {
      employeeId: this.employeeId,
      customerName: customerName,
      mobileNumber: mobileNumber,
      totalAmount: this.totalAmount,
     
      cartItems: this.cart().map(item => ({
        foodItemId: item.id,
        quantity: item.quantity,
      }))
    };

    this.orderService.placeOrder(orderPayload).subscribe({
      next: () => {
        alert('Order placed successfully!');
        this.clearCart();
        this.router.navigate(['/employee/dashboard']);
        
      },
      error: () => {
        alert('Something went wrong while placing the order.');
        
      }
    });
  }
}

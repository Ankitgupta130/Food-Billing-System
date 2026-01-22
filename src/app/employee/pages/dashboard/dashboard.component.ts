import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { FoodItemService } from '../../../services/food-item.service';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { Router, RouterModule } from '@angular/router';
import { CartService } from '../../../services/cart.service';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, TableModule, DialogModule, ButtonModule, HttpClientModule, FormsModule, InputTextModule, ToastModule, RouterModule, TranslateModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  providers: [FoodItemService, MessageService]
})
export class DashboardComponent {
  foodItems: any[] = [];
  cart = this.cartService.cartItems;
  cartDialogVisible: boolean = false;

  constructor(public foodItemService: FoodItemService, private readonly messageService: MessageService, private readonly router: Router, public cartService: CartService) {}

  ngOnInit() {
    this.getFoodItems();
  }

  getFoodItems() {
    this.foodItemService.getAll().subscribe({
      next:(data) => {
        this.foodItems = data.map((item:any)=>({...item, quantity:1}))
      },
      error: (error) =>{
        console.error('Error fetching food items',error)
      }
    })
  }

  addToCart(item:any) {
    const newItem = {...item, quantity: item.quantity || 1};
    this.cartService.addItem(newItem);
    this.messageService.add({
      severity:'success',
      summary: 'Added to Cart',
      detail: `${item.name} added to cart`,
      life: 1000
    })
  }

  getTotal() {
    return this.cart().reduce((acc, item)=> acc + (item.price * item.quantity),0)
  }

  goToCart() {
    this.router.navigate(['/employee/cart'])
  }

  clearCart() {
    this.cartService.clearCart();
    this.messageService.add({
      severity: 'info',
      summary: 'Cart Cleared',
      detail: "Cart Cleared",
      life: 1000
    })
  }

  removeFromCart(itemId: number, itemName: string){
    this.cartService.removeItem(itemId)
    this.messageService.add({
      severity:'warn',
      summary:'Item Removed',
      detail: `${itemName} has been removed from the cart`,
      life: 1000
    })
  }

}

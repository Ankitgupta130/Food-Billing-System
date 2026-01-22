import { Injectable, signal } from '@angular/core';

interface Item {
  id: number;
  name: string;
  price: number;
  quantity: number;
  // totalAmount:number;
}

@Injectable({
  providedIn: 'root'
})
export class 
CartService {

  cartItems = signal<Item[]>([]);

  addItem(item: Item) {
    const current = this.cartItems();
    const existing = current.find(i =>i.id === item.id);

    if(existing) {
      this.cartItems.set(current.map(i=>
        i.id ===item.id ? {...i, quantity: i.quantity + item.quantity} : i
      ));
    } else {
      this.cartItems.set([...current, item])
    }
  }

  removeItem(itemId : number) {
    this.cartItems.set(this.cartItems().filter(i =>i.id !== itemId))
  }

  clearCart() {
    this.cartItems.set([])
  }

  updateQuantity(itemId: number, quantity: number) {
    this.cartItems.set(
      this.cartItems().map(item=>
        item.id === itemId ? {...item, quantity} : item
      )
    );
  }

  constructor() { }
}

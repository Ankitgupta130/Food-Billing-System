export interface OrderItem {
    foodName: string;
    foodPrice: number;
    quantity: number;
    itemTotal: number
}

export interface OrderHistoryResponse {
    orderId: number;
    customerName: string;
    mobileNumber: string;
    employeeId: number;
    employeeName: string;
    orderDate: string;
    totalAmount: number;
    items: OrderItem[]
}
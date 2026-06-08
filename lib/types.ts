export interface MenuItem {
    id: number
    name: string
    price: number
    image: string
    category?: string
}

export interface OrderItem extends MenuItem {
    quantity: number
}

export interface Order {
    id: string
    customerName: string
    tableNumber?: string
    type: "Dine-in" | "Takeaway" | "Delivery"
    items: OrderItem[]
    total: number
    status: "Pending" | "Preparing" | "Ready" | "Served" | "Cancelled"
    createdAt: Date
}

export interface Stat {
    title: string
    value: string | number
    icon?: string
}

export interface Category {
    id: string
    name: string
    icon?: string
}

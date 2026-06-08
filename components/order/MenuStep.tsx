"use client"

import { useState } from "react"
import MenuLayout from "../menu/MenuLayout"
import CategorySidebar from "../menu/CategorySidebar"
import MenuGrid from "../menu/MenuGrid"
import BillSidebar from "./BillSidebar"
import { OrderItem, MenuItem } from "@/lib/types"

const dummyItems: MenuItem[] = [
    { id: 1, name: "Gourmet Burger", price: 12.50, image: "/food1.jpg", category: "Burgers" },
    { id: 2, name: "Truffle Pizza", price: 18.00, image: "/food2.jpg", category: "Pizza" },
    { id: 3, name: "Caesar Salad", price: 9.00, image: "/food1.jpg", category: "Salads" },
    { id: 4, name: "Pasta Carbonara", price: 14.50, image: "/food2.jpg", category: "Pasta" },
]

interface MenuStepProps {
    next: () => void
}

export default function MenuStep({ next }: MenuStepProps) {
    const [cart, setCart] = useState<OrderItem[]>([])
    const [selectedCategory, setSelectedCategory] = useState("All")

    const filteredItems = selectedCategory === "All" 
        ? dummyItems 
        : dummyItems.filter(item => item.category === selectedCategory)

    const addItem = (item: MenuItem) => {
        setCart((prev) => {
            const exist = prev.find((i) => i.id === item.id)
            if (exist) {
                return prev.map((i) =>
                    i.id === item.id ? { ...i, quantity: i.quantity + 1 } : i
                )
            }
            return [...prev, { ...item, quantity: 1 }]
        })
    }

    const updateQty = (id: number, delta: number) => {
        setCart((prev) =>
            prev
                .map((i) =>
                    i.id === id ? { ...i, quantity: i.quantity + delta } : i
                )
                .filter((i) => i.quantity > 0)
        )
    }

    return (
        <div className="h-full animate-in fade-in slide-in-from-bottom-4 duration-500">
            <MenuLayout
                left={
                    <CategorySidebar 
                        selectedCategory={selectedCategory} 
                        onSelect={(c) => setSelectedCategory(c)} 
                    />
                }
                center={
                    <MenuGrid 
                        items={filteredItems} 
                        onAdd={addItem} 
                    />
                }
                right={
                    <BillSidebar
                        cart={cart}
                        updateQty={updateQty}
                        onSubmit={next}
                    />
                }
            />
        </div>
    )
}
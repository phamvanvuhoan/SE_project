"use client"

import StatCard from "@/components/dashboard/StatCard"
import OrderTable from "@/components/dashboard/OrderTable"
import CreateOrderModal from "@/components/order/CreateOrderModal"
import MenuLayout from "@/components/menu/MenuLayout"
import CategorySidebar from "@/components/menu/CategorySidebar"
import MenuGrid from "@/components/menu/MenuGrid"
import { useState } from "react"
import { Plus, LayoutGrid, Users, ShoppingBag, DollarSign, Clock } from "lucide-react"

const dummyItems = [
    { id: 1, name: "Gourmet Burger", price: 12.50, image: "/food1.jpg", category: "Burgers" },
    { id: 2, name: "Truffle Pizza", price: 18.00, image: "/food2.jpg", category: "Pizza" },
    { id: 3, name: "Caesar Salad", price: 9.00, image: "/food1.jpg", category: "Salads" },
    { id: 4, name: "Pasta Carbonara", price: 14.50, image: "/food2.jpg", category: "Pasta" },
]

export default function DashboardPage() {
    const [open, setOpen] = useState(false)
    const [openMenu, setOpenMenu] = useState(false)
    const [selectedCategory, setSelectedCategory] = useState("All")

    const filteredItems = selectedCategory === "All" 
        ? dummyItems 
        : dummyItems.filter(item => item.category === selectedCategory)

    return (
        <div className="space-y-8 animate-in fade-in duration-500">
            <div className="flex justify-between items-center">
                <h2 className="text-3xl font-bold text-gray-800">Overview</h2>
                <div className="flex gap-3">
                    <button
                        onClick={() => setOpenMenu(true)}
                        className="flex items-center gap-2 bg-white text-gray-700 px-4 py-2.5 rounded-xl border border-gray-200 shadow-sm hover:shadow-md transition-all active:scale-95"
                    >
                        <LayoutGrid size={18} />
                        View Menu
                    </button>
                    <button
                        onClick={() => setOpen(true)}
                        className="flex items-center gap-2 bg-orange-500 text-white px-5 py-2.5 rounded-xl shadow-lg shadow-orange-200 hover:bg-orange-600 transition-all active:scale-95 font-semibold"
                    >
                        <Plus size={18} />
                        New Order
                    </button>
                </div>
            </div>

            {/* Top Stats */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard 
                    title="Total Occupancy" 
                    value="5/10" 
                    icon={Users} 
                    trend={{ value: 12, isPositive: true }} 
                />
                <StatCard 
                    title="Active Orders" 
                    value="12" 
                    icon={ShoppingBag} 
                    trend={{ value: 8, isPositive: true }} 
                />
                <StatCard 
                    title="Today's Revenue" 
                    value="$500.00" 
                    icon={DollarSign} 
                    trend={{ value: 5, isPositive: false }} 
                />
                <StatCard 
                    title="Kitchen Queue" 
                    value="6 orders" 
                    icon={Clock} 
                />
            </div>


            {/* Order Table section */}
            <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
                <div className="p-6 border-b border-gray-100">
                    <h3 className="text-xl font-bold text-gray-800">Recent Orders</h3>
                </div>
                <OrderTable />
            </div>

            {/* Modal */}
            {open && <CreateOrderModal onClose={() => setOpen(false)} />}
            
            {openMenu && (
                <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-in fade-in duration-300">
                    <div className="bg-white w-full max-w-[90vw] h-[90vh] rounded-3xl overflow-hidden relative shadow-2xl animate-in zoom-in-95 duration-300">
                        <button
                            onClick={() => setOpenMenu(false)}
                            className="absolute z-60 top-6 right-6 w-10 h-10 flex items-center justify-center bg-white rounded-full shadow-lg text-gray-500 hover:text-red-500 transition-colors border border-gray-100"
                        >
                            <Plus className="rotate-45" size={20} />
                        </button>

                        <div className="h-full pt-4">
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
                                        onAdd={() => {}} 
                                    />
                                }
                                right={<div className="p-6 bg-gray-50 h-full">Cart Details Placeholder</div>}
                            />
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
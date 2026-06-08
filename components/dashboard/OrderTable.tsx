"use client"

import { useState } from "react"
import { X, Receipt, User, Table as TableIcon, Clock, ChevronRight, Hash, DollarSign, ListOrdered } from "lucide-react"
import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

interface OrderItem {
    id: number
    name: string
    price: number
    quantity: number
}

interface Order {
    id: number
    table: string
    customer: string
    amount: string
    status: "Pending" | "Served" | "Preparing" | "Cancelled"
    time: string
    items: OrderItem[]
}

const orders: Order[] = [
    { 
        id: 1, table: "A1", customer: "John Doe", amount: "$52.50", status: "Pending", time: "10:15 AM",
        items: [
            { id: 1, name: "Gourmet Burger", price: 12.50, quantity: 2 },
            { id: 2, name: "Truffle Pizza", price: 18.00, quantity: 1 },
            { id: 3, name: "Caesar Salad", price: 9.50, quantity: 1 }
        ]
    },
    { 
        id: 2, table: "B2", customer: "Anna Smith", amount: "$84.00", status: "Served", time: "09:45 AM",
        items: [
            { id: 1, name: "Gourmet Burger", price: 12.50, quantity: 4 },
            { id: 4, name: "Pasta Carbonara", price: 14.50, quantity: 2 },
            { id: 5, name: "Espresso", price: 2.50, quantity: 2 }
        ]
    },
    { 
        id: 3, table: "C3", customer: "Robert Brown", amount: "$32.00", status: "Preparing", time: "10:30 AM",
        items: [
            { id: 2, name: "Truffle Pizza", price: 18.00, quantity: 1 },
            { id: 4, name: "Pasta Carbonara", price: 14.00, quantity: 1 }
        ]
    },
    { 
        id: 4, table: "A2", customer: "Sarah Wilson", amount: "$12.50", status: "Cancelled", time: "10:05 AM",
        items: [
            { id: 1, name: "Gourmet Burger", price: 12.50, quantity: 1 }
        ]
    },
]

const statusStyles = {
    Pending: "bg-amber-50 text-amber-600 border-amber-100",
    Served: "bg-green-50 text-green-600 border-green-100",
    Preparing: "bg-blue-50 text-blue-600 border-blue-100",
    Cancelled: "bg-red-50 text-red-600 border-red-100",
}

export default function OrderTable() {
    const [selectedOrder, setSelectedOrder] = useState<Order | null>(null)

    return (
        <div className="relative h-full">
            <div className="w-full overflow-x-auto">
                <table className="w-full text-left border-collapse min-w-[800px]">
                    <thead>
                        <tr className="bg-gray-50/50 border-b border-gray-100">
                            <th className="px-6 py-5 text-xs font-black text-gray-400 uppercase tracking-widest">Order ID</th>
                            <th className="px-6 py-5 text-xs font-black text-gray-400 uppercase tracking-widest">Table</th>
                            <th className="px-6 py-5 text-xs font-black text-gray-400 uppercase tracking-widest">Customer</th>
                            <th className="px-6 py-5 text-xs font-black text-gray-400 uppercase tracking-widest">Amount</th>
                            <th className="px-6 py-5 text-xs font-black text-gray-400 uppercase tracking-widest">Time</th>
                            <th className="px-6 py-5 text-xs font-black text-gray-400 uppercase tracking-widest">Status</th>
                            <th className="px-6 py-5"></th>
                        </tr>
                    </thead>

                    <tbody className="divide-y divide-gray-100 bg-white">
                        {orders.map((o) => (
                            <tr 
                                key={o.id} 
                                onClick={() => setSelectedOrder(o)}
                                className={cn(
                                    "hover:bg-orange-50/30 transition-all cursor-pointer group/row animate-in fade-in duration-300",
                                    selectedOrder?.id === o.id ? "bg-orange-50/50" : ""
                                )}
                            >
                                <td className="px-6 py-5">
                                    <div className="flex items-center gap-2">
                                        <div className="w-8 h-8 rounded-lg bg-gray-50 flex items-center justify-center text-gray-300 group-hover/row:text-orange-500 transition-colors">
                                            <Hash size={14} />
                                        </div>
                                        <span className="font-black text-xs text-gray-500">{o.id.toString().padStart(4, '0')}</span>
                                    </div>
                                </td>
                                <td className="px-6 py-5">
                                    <span className="px-3 py-1 rounded-lg bg-gray-50 border border-gray-100 text-sm font-black text-gray-800 tracking-tight">{o.table}</span>
                                </td>
                                <td className="px-6 py-5 font-bold text-gray-700">{o.customer}</td>
                                <td className="px-6 py-5 font-black text-gray-900">{o.amount}</td>
                                <td className="px-6 py-5">
                                    <div className="flex items-center gap-2 text-xs font-bold text-gray-400">
                                        <Clock size={12} />
                                        {o.time}
                                    </div>
                                </td>
                                <td className="px-6 py-5">
                                    <span className={cn(
                                        "px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-widest border shadow-sm transition-all group-hover/row:shadow-md",
                                        statusStyles[o.status]
                                    )}>
                                        {o.status}
                                    </span>
                                </td>
                                <td className="px-6 py-5 text-right opacity-0 group-hover/row:opacity-100 scale-90 group-hover/row:scale-100 transition-all">
                                    <div className="inline-flex items-center gap-2 bg-orange-500 text-white px-4 py-2 rounded-xl text-xs font-black uppercase tracking-widest shadow-lg shadow-orange-100">
                                        Open Details
                                        <ChevronRight size={14} />
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* Centered Modal Detail View */}
            {selectedOrder && (
                <div className="fixed inset-0 z-[100] flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-in fade-in duration-300">
                    <div className="bg-white rounded-[40px] shadow-2xl overflow-hidden relative animate-in zoom-in-95 duration-300 transition-all w-[98vw] h-[95vh] flex flex-col">
                        
                        {/* Modal Header */}
                        <div className="flex justify-between items-start px-12 py-10 bg-gray-50/50 border-b border-gray-100">
                            <div>
                                <div className="flex items-center gap-3 mb-4">
                                    <div className="w-12 h-12 bg-white rounded-2xl flex items-center justify-center shadow-sm border border-gray-100">
                                        <Receipt className="text-orange-500" size={24} />
                                    </div>
                                    <div>
                                        <h3 className="text-3xl font-black text-gray-900 tracking-tighter">Order Summary</h3>
                                        <p className="text-xs font-bold text-gray-400 uppercase tracking-[0.2em]">Transaction Record #ORD-{selectedOrder.id.toString().padStart(4, '0')}</p>
                                    </div>
                                </div>
                                <div className="flex gap-3">
                                    <span className={cn(
                                        "px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-widest border shadow-sm",
                                        statusStyles[selectedOrder.status]
                                    )}>
                                        {selectedOrder.status}
                                    </span>
                                    <span className="px-4 py-1.5 rounded-full text-[10px] font-black uppercase tracking-widest border border-gray-100 bg-white text-gray-500 shadow-sm flex items-center gap-2">
                                        <Clock size={12} />
                                        Logged at {selectedOrder.time}
                                    </span>
                                </div>
                            </div>

                            <button 
                                onClick={() => setSelectedOrder(null)}
                                className="w-14 h-14 flex items-center justify-center rounded-2xl bg-white text-gray-400 hover:text-red-500 shadow-sm border border-gray-100 transition-all active:scale-90 hover:rotate-90 duration-300"
                            >
                                <X size={28} />
                            </button>
                        </div>

                        <div className="flex-1 flex overflow-hidden">
                            {/* Left Side: Items & Receipt */}
                            <div className="flex-1 p-12 overflow-y-auto custom-scrollbar border-r border-gray-50">
                                <div className="max-w-2xl">
                                    <div className="flex items-center gap-3 mb-10">
                                        <div className="w-8 h-8 rounded-lg bg-orange-50 flex items-center justify-center text-orange-500">
                                            <ListOrdered size={16} />
                                        </div>
                                        <h4 className="text-lg font-black text-gray-900 uppercase tracking-tight">Ordered Items ({selectedOrder.items.length})</h4>
                                    </div>

                                    <div className="space-y-6">
                                        {selectedOrder.items.map((item) => (
                                            <div key={item.id} className="flex justify-between items-center bg-gray-50/50 p-6 rounded-3xl border border-gray-100/50 hover:bg-white hover:border-orange-200 transition-all group">
                                                <div className="flex items-center gap-6">
                                                    <div className="w-14 h-14 bg-white rounded-2xl flex items-center justify-center font-black text-gray-400 group-hover:text-orange-500 transition-all border border-gray-100 shadow-sm">
                                                        {item.quantity}x
                                                    </div>
                                                    <div>
                                                        <p className="text-xl font-black text-gray-800 leading-tight tracking-tight">{item.name}</p>
                                                        <p className="text-xs font-bold text-gray-400 uppercase tracking-widest mt-2 flex items-center gap-1">
                                                            <DollarSign size={12} />
                                                            Unit Price: ${item.price.toFixed(2)}
                                                        </p>
                                                    </div>
                                                </div>
                                                <div className="text-2xl font-black text-gray-900 tracking-tighter">${(item.price * item.quantity).toFixed(2)}</div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            {/* Right Side: Meta Info & Actions */}
                            <div className="w-[450px] bg-gray-50/30 p-12 flex flex-col">
                                <div className="space-y-8 flex-1">
                                    {/* Customer Info */}
                                    <div className="space-y-4">
                                        <label className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] ml-1">Customer / Assignee</label>
                                        <div className="flex items-center gap-4 bg-white p-6 rounded-3xl border border-gray-100 shadow-sm">
                                            <div className="w-12 h-12 bg-orange-50 rounded-2xl flex items-center justify-center text-orange-500">
                                                <User size={24} />
                                            </div>
                                            <div>
                                                <p className="text-lg font-black text-gray-900 leading-none">{selectedOrder.customer}</p>
                                                <p className="text-xs font-bold text-gray-400 mt-2">Verified Patron</p>
                                            </div>
                                        </div>
                                    </div>

                                    {/* Table Table Info */}
                                    <div className="space-y-4">
                                        <label className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] ml-1">Location Details</label>
                                        <div className="flex items-center gap-4 bg-white p-6 rounded-3xl border border-gray-100 shadow-sm">
                                            <div className="w-12 h-12 bg-blue-50 rounded-2xl flex items-center justify-center text-blue-500">
                                                <TableIcon size={24} />
                                            </div>
                                            <div>
                                                <p className="text-lg font-black text-gray-900 leading-none">Table {selectedOrder.table}</p>
                                                <p className="text-xs font-bold text-gray-400 mt-2">Zone: Main Dining Hall</p>
                                            </div>
                                        </div>
                                    </div>

                                    {/* Financial Summary */}
                                    <div className="mt-12 bg-gray-900 rounded-[32px] p-8 shadow-2xl relative overflow-hidden group">
                                        <div className="absolute top-0 right-0 w-32 h-32 bg-white/5 rounded-full -mr-16 -mt-16 blur-2xl group-hover:scale-150 transition-transform duration-700" />
                                        <p className="text-[10px] font-black text-white/40 uppercase tracking-[0.2em] mb-4">Total Amount Payable</p>
                                        <div className="flex items-baseline gap-1">
                                            <span className="text-white/60 font-black text-2xl">$</span>
                                            <span className="text-5xl font-black text-white tracking-widest">{selectedOrder.amount.replace('$', '')}</span>
                                        </div>
                                        <div className="mt-8 flex gap-2">
                                            <span className="bg-green-500/20 text-green-400 px-3 py-1 rounded-lg text-[10px] font-black uppercase tracking-widest border border-green-500/30 font-mono">Paid</span>
                                            <span className="bg-white/10 text-white/60 px-3 py-1 rounded-lg text-[10px] font-black uppercase tracking-widest border border-white/10 font-mono">Visa **** 4421</span>
                                        </div>
                                    </div>
                                </div>

                                <div className="mt-10 pt-10 border-t border-gray-100 space-y-4">
                                    <button className="w-full bg-orange-500 text-white py-6 rounded-3xl font-black text-xl shadow-xl shadow-orange-100 hover:bg-orange-600 active:scale-95 transition-all flex items-center justify-center gap-3">
                                        <Receipt size={24} />
                                        Print POS Receipt
                                    </button>
                                    <button className="w-full bg-white text-gray-500 py-4 rounded-2xl font-bold text-sm border border-gray-100 hover:text-gray-900 transition-all">
                                        Send Digital Receipt via SMS
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
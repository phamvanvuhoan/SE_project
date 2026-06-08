import { OrderItem } from "@/lib/types"
import { Trash2, Plus, Minus } from "lucide-react"

interface BillSidebarProps {
    cart: OrderItem[]
    updateQty: (id: number, delta: number) => void
    onSubmit: () => void
}

export default function BillSidebar({ cart, updateQty, onSubmit }: BillSidebarProps) {
    const total = cart.reduce(
        (sum: number, item: OrderItem) => sum + item.price * item.quantity,
        0
    )

    return (
        <div className="flex flex-col h-full bg-white p-6">
            <h3 className="text-xl font-bold text-gray-800 mb-6">Current Bill</h3>

            <div className="flex-1 space-y-4 overflow-y-auto pr-2 custom-scrollbar">
                {cart.length === 0 ? (
                    <div className="h-full flex flex-col items-center justify-center text-gray-400 gap-3">
                        <Trash2 size={48} strokeWidth={1} />
                        <p className="text-sm font-medium">Your cart is empty</p>
                    </div>
                ) : (
                    cart.map((item) => (
                        <div key={item.id} className="flex justify-between items-center group animate-in slide-in-from-right-4 duration-300">
                            <div className="flex-1">
                                <p className="font-bold text-gray-800 leading-tight">{item.name}</p>
                                <p className="text-sm font-semibold text-orange-500">${item.price.toFixed(2)}</p>
                            </div>

                            <div className="flex items-center gap-3 bg-gray-50 p-1 rounded-xl">
                                <button 
                                    onClick={() => updateQty(item.id, -1)}
                                    className="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-white hover:shadow-sm text-gray-400 hover:text-gray-900 transition-all"
                                >
                                    <Minus size={14} />
                                </button>
                                <span className="w-6 text-center font-bold text-gray-800 text-sm">{item.quantity}</span>
                                <button 
                                    onClick={() => updateQty(item.id, 1)}
                                    className="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-white hover:shadow-sm text-gray-400 hover:text-gray-900 transition-all"
                                >
                                    <Plus size={14} />
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>

            <div className="mt-6 pt-6 border-t border-dashed border-gray-200">
                <div className="flex justify-between items-center mb-6">
                    <span className="text-gray-500 font-medium">Total Amount</span>
                    <span className="text-2xl font-black text-gray-900">${total.toFixed(2)}</span>
                </div>

                <div className="flex gap-3">
                    <button className="flex-1 bg-gray-50 text-gray-400 py-3.5 rounded-2xl font-bold hover:bg-gray-100 transition-all active:scale-95">
                        Cancel
                    </button>
                    <button
                        onClick={onSubmit}
                        disabled={cart.length === 0}
                        className="flex-1 bg-gray-900 text-white py-3.5 rounded-2xl font-bold hover:bg-orange-500 disabled:opacity-50 disabled:hover:bg-gray-900 transition-all active:scale-95 shadow-lg shadow-gray-200"
                    >
                        Confirm Order
                    </button>
                </div>
            </div>
        </div>
    )
}
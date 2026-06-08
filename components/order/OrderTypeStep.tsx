import { Utensils,  ShoppingBag, Truck } from "lucide-react"

interface OrderTypeStepProps {
    next: () => void
}

export default function OrderTypeStep({ next }: OrderTypeStepProps) {
    const types = [
        { name: "Dine-in", icon: Utensils, description: "Eat at the restaurant" },
        { name: "Takeaway", icon: ShoppingBag, description: "Order and pick up" },
        { name: "Delivery", icon: Truck, description: "Send to customer location" },
    ]

    return (
        <div className="animate-in fade-in slide-in-from-bottom-4 duration-500">
            <h2 className="text-xl font-bold text-gray-800 mb-6">Select Order Type</h2>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {types.map((t) => (
                    <button
                        key={t.name}
                        onClick={next}
                        className="flex flex-col items-center gap-4 p-6 rounded-2xl border-2 border-gray-50 hover:border-orange-500 hover:bg-orange-50/30 transition-all group active:scale-95 text-center"
                    >
                        <div className="w-16 h-16 rounded-full bg-gray-50 flex items-center justify-center text-gray-400 group-hover:bg-white group-hover:text-orange-500 shadow-sm transition-colors border border-gray-100">
                            <t.icon size={28} />
                        </div>
                        <div>
                            <p className="font-bold text-gray-800 text-lg mb-1">{t.name}</p>
                            <p className="text-xs text-gray-400 font-medium leading-tight">{t.description}</p>
                        </div>
                    </button>
                ))}
            </div>
        </div>
    )
}
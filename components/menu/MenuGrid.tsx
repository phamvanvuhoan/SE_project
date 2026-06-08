import MenuCard from "./MenuCard"
import { MenuItem } from "@/lib/types"
import { Plus } from "lucide-react"

interface MenuGridProps {
    items: MenuItem[]
    onAdd: (item: MenuItem) => void
    onAddOption?: () => void
}

export default function MenuGrid({ items, onAdd, onAddOption }: MenuGridProps) {
    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 p-6">
            {onAddOption && (
                <button
                    onClick={onAddOption}
                    className="group h-[320px] bg-white rounded-3xl border-2 border-dashed border-gray-200 hover:border-orange-500 hover:bg-orange-50/30 transition-all flex flex-col items-center justify-center p-6 text-center animate-in fade-in duration-500"
                >
                    <div className="w-16 h-16 rounded-full bg-gray-50 flex items-center justify-center text-gray-300 group-hover:bg-white group-hover:text-orange-500 shadow-sm transition-all mb-4 border border-gray-100">
                        <Plus size={32} />
                    </div>
                    <div className="space-y-1">
                        <p className="font-bold text-gray-800 text-lg">Add New Option</p>
                        <p className="text-xs text-gray-400 font-medium">Create a new menu item</p>
                    </div>
                </button>
            )}

            {items.map((item) => (
                <MenuCard key={item.id} item={item} onAdd={onAdd} />
            ))}
        </div>
    )
}
import Image from "next/image"
import { MenuItem } from "@/lib/types"

interface MenuCardProps {
    item: MenuItem
    onAdd: (item: MenuItem) => void
}

export default function MenuCard({ item, onAdd }: MenuCardProps) {
    return (
        <div className="group bg-white rounded-2xl overflow-hidden border border-gray-100 shadow-sm hover:shadow-xl transition-all duration-300 hover:-translate-y-1">
            <div className="relative h-48 w-full bg-gray-100">
                <Image
                    src={item.image}
                    alt={item.name}
                    fill
                    className="object-cover transition-transform duration-500 group-hover:scale-110"
                />
                <div className="absolute top-3 right-3 bg-white/90 backdrop-blur px-2 py-1 rounded-lg text-xs font-bold text-gray-800 shadow-sm">
                    ${item.price.toFixed(2)}
                </div>
            </div>

            <div className="p-5">
                <h4 className="font-bold text-gray-800 mb-1">{item.name}</h4>
                <p className="text-xs text-gray-400 mb-4 line-clamp-1">{item.category || 'General'}</p>

                <button
                    onClick={() => onAdd(item)}
                    className="w-full bg-gray-900 text-white py-2.5 rounded-xl font-semibold hover:bg-orange-500 transition-colors active:scale-95 flex items-center justify-center gap-2"
                >
                    Add to Order
                </button>
            </div>
        </div>
    )
}
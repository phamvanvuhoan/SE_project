import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

const categories = ["All", "Burgers", "Pizza", "Drinks", "Salads", "Desserts"]

interface CategorySidebarProps {
    selectedCategory?: string
    onSelect: (category: string) => void
}

export default function CategorySidebar({ selectedCategory = "All", onSelect }: CategorySidebarProps) {
    return (
        <div className="p-4">
            <h3 className="text-xs font-bold text-gray-400 uppercase tracking-widest mb-6 px-4">Categories</h3>

            <div className="space-y-1">
                {categories.map((c) => (
                    <button
                        key={c}
                        onClick={() => onSelect(c)}
                        className={cn(
                            "w-full text-left px-4 py-3 rounded-xl transition-all font-medium",
                            selectedCategory === c 
                                ? "bg-orange-500 text-white shadow-lg shadow-orange-100" 
                                : "text-gray-600 hover:bg-gray-50 hover:text-gray-900"
                        )}
                    >
                        {c}
                    </button>
                ))}
            </div>
        </div>
    )
}
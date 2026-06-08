import { MenuItem } from "@/lib/types"
import { Save, X, Image as ImageIcon, Tag, DollarSign, Package } from "lucide-react"
import { useState, useEffect } from "react"

interface EditorSidebarProps {
    selected: MenuItem | null
    onSave: (updatedItem: MenuItem) => void
    onCancel?: () => void
}

export default function EditorSidebar({ selected, onSave, onCancel }: EditorSidebarProps) {
    const [formData, setFormData] = useState<MenuItem | null>(null)

    // Sync local state when selected item changes
    useEffect(() => {
        setFormData(selected)
    }, [selected])

    if (!formData) {
        return (
            <div className="flex flex-col items-center justify-center h-full text-gray-400 p-8 text-center bg-white border-l border-gray-100">
                <div className="w-20 h-20 bg-gray-50 rounded-full flex items-center justify-center mb-6 shadow-inner">
                    <Package size={32} className="text-gray-200" />
                </div>
                <p className="font-black text-gray-900 uppercase tracking-tighter text-xl">Item Details</p>
                <p className="text-sm font-semibold text-gray-400 mt-2 italic">Choose a product from the grid<br/>to start managing its properties</p>
            </div>
        )
    }

    const handleChange = (field: keyof MenuItem, value: string | number) => {
        setFormData(prev => prev ? { ...prev, [field]: value } : null)
    }

    return (
        <div className="flex flex-col h-full bg-white p-6 animate-in slide-in-from-right-4 duration-500 border-l border-gray-100 shadow-2xl relative z-10">
            <div className="flex justify-between items-center mb-10">
                <div className="flex items-center gap-3">
                    <div className="p-3 bg-orange-50 text-orange-500 rounded-xl shadow-sm border border-orange-100">
                        <Tag size={20} />
                    </div>
                    <h3 className="text-xl font-black text-gray-800 tracking-tighter uppercase">Properties</h3>
                </div>
                {onCancel && (
                    <button 
                        onClick={onCancel}
                        className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-50 text-gray-400 hover:text-red-500 hover:bg-red-50 transition-all active:scale-90"
                    >
                        <X size={20} />
                    </button>
                )}
            </div>

            <div className="space-y-8 flex-1 overflow-y-auto px-1 custom-scrollbar">
                {/* Item Name */}
                <div className="space-y-3">
                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] ml-1">Display Name</label>
                    <div className="relative group">
                        <Package className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300 group-focus-within:text-orange-500 transition-colors" size={18} />
                        <input
                            value={formData.name}
                            onChange={(e) => handleChange("name", e.target.value)}
                            placeholder="e.g. Double Cheeseburger"
                            className="w-full bg-gray-50 border-2 border-gray-100 p-4 pl-12 rounded-2xl focus:bg-white focus:border-orange-500 transition-all outline-none font-bold text-gray-800 placeholder:text-gray-300"
                        />
                    </div>
                </div>

                {/* Price */}
                <div className="space-y-3">
                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] ml-1">Retail Price ($)</label>
                    <div className="relative group">
                        <DollarSign className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300 group-focus-within:text-orange-500 transition-colors" size={18} />
                        <input
                            type="number"
                            step="0.01"
                            value={formData.price}
                            onChange={(e) => handleChange("price", parseFloat(e.target.value) || 0)}
                            className="w-full bg-gray-50 border-2 border-gray-100 p-4 pl-12 rounded-2xl focus:bg-white focus:border-orange-500 transition-all outline-none font-bold text-gray-800"
                        />
                    </div>
                </div>

                {/* Category */}
                <div className="space-y-3">
                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] ml-1">Menu Category</label>
                    <div className="relative group">
                        <Tag className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300 group-focus-within:text-orange-500 transition-colors" size={18} />
                        <input
                            value={formData.category || ""}
                            onChange={(e) => handleChange("category", e.target.value)}
                            placeholder="e.g. Burgers, Drinks"
                            className="w-full bg-gray-50 border-2 border-gray-100 p-4 pl-12 rounded-2xl focus:bg-white focus:border-orange-500 transition-all outline-none font-bold text-gray-800 placeholder:text-gray-300"
                        />
                    </div>
                </div>

                {/* Image URL Placeholder */}
                <div className="space-y-3">
                    <label className="text-[10px] font-black text-gray-400 uppercase tracking-[0.2em] ml-1">Media Source (URL)</label>
                    <div className="relative group">
                        <ImageIcon className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-300 group-focus-within:text-orange-500 transition-colors" size={18} />
                        <input
                            value={formData.image}
                            onChange={(e) => handleChange("image", e.target.value)}
                            className="w-full bg-gray-50 border-2 border-gray-100 p-4 pl-12 rounded-2xl focus:bg-white focus:border-orange-500 transition-all outline-none font-bold text-gray-800"
                        />
                    </div>
                </div>
            </div>

            <div className="pt-8 border-t border-gray-100 mt-auto">
                <button
                    onClick={() => onSave(formData)}
                    className="w-full bg-gray-900 text-white py-5 rounded-2xl font-black text-lg flex items-center justify-center gap-3 hover:bg-orange-500 transition-all active:scale-95 shadow-xl shadow-gray-100 border border-transparent hover:shadow-orange-200"
                >
                    <Save size={22} className="opacity-50" />
                    Apply Changes
                </button>
            </div>
        </div>
    )
}
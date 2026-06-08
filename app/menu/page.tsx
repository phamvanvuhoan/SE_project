"use client"

import { useState } from "react"
import { CheckCircle2, ChevronRight, PackagePlus } from "lucide-react"
import MenuLayout from "@/components/menu/MenuLayout"
import CategorySidebar from "@/components/menu/CategorySidebar"
import MenuGrid from "@/components/menu/MenuGrid"
import EditorSidebar from "@/components/menu-editor/EditorSidebar"
import { MenuItem } from "@/lib/types"

const INITIAL_ITEMS: MenuItem[] = [
    { id: 1, name: "Gourmet Burger", price: 12.50, image: "/food1.jpg", category: "Burgers" },
    { id: 2, name: "Truffle Pizza", price: 18.00, image: "/food2.jpg", category: "Pizza" },
    { id: 3, name: "Caesar Salad", price: 9.00, image: "/food1.jpg", category: "Salads" },
    { id: 4, name: "Pasta Carbonara", price: 14.50, image: "/food2.jpg", category: "Pasta" },
]

export default function MenuPage() {
    const [items, setItems] = useState<MenuItem[]>(INITIAL_ITEMS)
    const [selected, setSelected] = useState<MenuItem | null>(null)
    const [selectedCategory, setSelectedCategory] = useState("All")
    const [notification, setNotification] = useState<string | null>(null)
    const [isAddingMode, setIsAddingMode] = useState(false)

    const filteredItems = selectedCategory === "All" 
        ? items 
        : items.filter(item => item.category === selectedCategory)

    const handleAddOption = () => {
        const newItem: MenuItem = {
            id: Date.now(),
            name: "New Menu Item",
            price: 0,
            image: "/food1.jpg", 
            category: selectedCategory === "All" ? "General" : selectedCategory
        }
        setSelected(newItem)
        setIsAddingMode(true)
    }

    const handleSave = (updatedItem: MenuItem) => {
        if (isAddingMode) {
            setItems(prev => [updatedItem, ...prev])
            showNotification(`&quot;${updatedItem.name}&quot; added to the menu!`)
        } else {
            // Correctly update the item in the local state array
            setItems(prev => prev.map(item => item.id === updatedItem.id ? { ...updatedItem } : item))
            showNotification(`Updated &quot;${updatedItem.name}&quot; successfully.`)
        }
        setIsAddingMode(false)
        setSelected(null)
    }

    const showNotification = (msg: string) => {
        setNotification(msg)
        setTimeout(() => setNotification(null), 3000)
    }

    return (
        <div className="h-full relative overflow-hidden bg-gray-50/30">
            {/* Breadcrumb Header */}
            <div className="absolute top-0 left-0 right-0 h-16 bg-white border-b border-gray-100 flex items-center px-10 z-20">
                <div className="flex items-center gap-3 text-xs font-black text-gray-400 uppercase tracking-widest">
                    <span>Menu Tools</span>
                    <ChevronRight size={14} className="text-gray-200" />
                    <span className="text-gray-900">Inventory Management</span>
                </div>
                
                <button 
                    onClick={handleAddOption}
                    className="ml-auto bg-gray-900 text-white px-5 py-2 rounded-xl text-xs font-black uppercase tracking-widest hover:bg-orange-500 transition-all flex items-center gap-2"
                >
                    <PackagePlus size={16} />
                    Direct Add
                </button>
            </div>

            {/* Notification Toast */}
            {notification && (
                <div className="absolute top-20 left-1/2 -translate-x-1/2 z-50 animate-in slide-in-from-top-full duration-500">
                    <div className="bg-gray-900 text-white px-8 py-4 rounded-3xl shadow-2xl flex items-center gap-4 border border-gray-800 shadow-orange-100/20">
                        <CheckCircle2 className="text-green-500" size={24} />
                        <span className="font-black text-xs uppercase tracking-widest" dangerouslySetInnerHTML={{ __html: notification }} />
                    </div>
                </div>
            )}

            <div className="pt-16 h-full">
                <MenuLayout
                    left={
                        <CategorySidebar 
                            selectedCategory={selectedCategory} 
                            onSelect={(c) => {
                                setSelectedCategory(c);
                                setSelected(null);
                            }} 
                        />
                    }
                    center={
                        <MenuGrid
                            items={filteredItems}
                            onAddOption={handleAddOption}
                            onAdd={(item: MenuItem) => {
                                setSelected(item)
                                setIsAddingMode(false)
                            }}
                        />
                    }
                    right={
                        <EditorSidebar
                            key={selected?.id || "empty"} // Key forces a clean re-mount for each item
                            selected={selected}
                            onSave={handleSave}
                            onCancel={() => {
                                setIsAddingMode(false)
                                setSelected(null)
                            }}
                        />
                    }
                />
            </div>
        </div>
    )
}
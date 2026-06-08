"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { 
    LayoutDashboard, 
    UtensilsCrossed, 
    Users, 
    Table, 
    BarChart3, 
    Settings,
    LogOut
} from "lucide-react"
import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

const menuItems = [
    { name: "Dashboard", href: "/dashboard", icon: LayoutDashboard },
    { name: "Menu", href: "/menu", icon: UtensilsCrossed },
    { name: "Customers", href: "/customers", icon: Users },
    { name: "Tables", href: "/tables", icon: Table },
    { name: "Analytics", href: "/analytics", icon: BarChart3 },
]

export default function Sidebar() {
    const pathname = usePathname()

    return (
        <div className="w-72 h-screen bg-white border-r border-gray-100 flex flex-col p-6 sticky top-0">
            <div className="flex items-center gap-3 mb-10 px-2">
                <div className="w-10 h-10 bg-orange-500 rounded-xl flex items-center justify-center text-white shadow-lg shadow-orange-200">
                    <UtensilsCrossed size={22} />
                </div>
                <h1 className="text-xl font-black text-gray-800 tracking-tight">RESTO.</h1>
            </div>

            <nav className="flex-1 space-y-2">
                {menuItems.map((item) => {
                    const isActive = pathname === item.href
                    return (
                        <Link
                            key={item.name}
                            href={item.href}
                            className={cn(
                                "flex items-center gap-3 px-4 py-3.5 rounded-2xl transition-all duration-200 group",
                                isActive 
                                    ? "bg-orange-50 text-orange-600 shadow-sm shadow-orange-50" 
                                    : "text-gray-500 hover:bg-gray-50 hover:text-gray-900"
                            )}
                        >
                            <item.icon 
                                size={20} 
                                className={cn(
                                    "transition-colors",
                                    isActive ? "text-orange-600" : "text-gray-400 group-hover:text-gray-600"
                                )} 
                            />
                            <span className="font-semibold tracking-tight">{item.name}</span>
                            {isActive && (
                                <div className="ml-auto w-1.5 h-1.5 rounded-full bg-orange-600" />
                            )}
                        </Link>
                    )
                })}
            </nav>

            <div className="mt-auto space-y-2">
                <button className="w-full flex items-center gap-3 px-4 py-3.5 rounded-2xl text-gray-500 hover:bg-gray-50 hover:text-gray-900 transition-all font-semibold tracking-tight group">
                    <Settings size={20} className="text-gray-400 group-hover:text-gray-600" />
                    Settings
                </button>
                <button className="w-full flex items-center gap-3 px-4 py-3.5 rounded-2xl text-red-500 hover:bg-red-50 transition-all font-semibold tracking-tight group">
                    <LogOut size={20} className="text-red-400 group-hover:text-red-600" />
                    Logout
                </button>
            </div>
        </div>
    )
}
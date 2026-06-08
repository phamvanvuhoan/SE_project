import { LucideIcon } from "lucide-react"

interface StatCardProps {
    title: string
    value: string | number
    icon?: LucideIcon
    trend?: {
        value: number
        isPositive: boolean
    }
}

export default function StatCard({ title, value, icon: Icon, trend }: StatCardProps) {
    return (
        <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
            <div className="flex justify-between items-start mb-4">
                <div className="p-3 bg-orange-50 text-orange-500 rounded-xl">
                    {Icon ? <Icon size={24} /> : <div className="w-6 h-6 bg-orange-200 rounded animate-pulse" />}
                </div>
                {trend && (
                    <span className={`text-xs font-semibold px-2 py-1 rounded-full ${trend.isPositive ? 'bg-green-50 text-green-600' : 'bg-red-50 text-red-600'}`}>
                        {trend.isPositive ? '+' : '-'}{trend.value}%
                    </span>
                )}
            </div>
            <div>
                <p className="text-sm font-medium text-gray-500 mb-1">{title}</p>
                <h3 className="text-2xl font-bold text-gray-800">{value}</h3>
            </div>
        </div>
    )
}
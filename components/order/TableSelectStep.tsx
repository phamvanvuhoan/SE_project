import { Table as TableIcon, CheckCircle2, XCircle } from "lucide-react"
import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

interface TableSelectStepProps {
    next: () => void
}

// Occupied tables for UI demonstration
const occupiedTables = ["A1", "B2", "C3", "A4"]

export default function TableSelectStep({ next }: TableSelectStepProps) {
    const tables = [
        "A1", "A2", "A3", "A4",
        "B1", "B2", "B3", "B4",
        "C1", "C2", "C3", "C4"
    ]

    return (
        <div className="animate-in fade-in slide-in-from-bottom-4 duration-500">
            <div className="flex items-center justify-between mb-8">
                <div className="flex items-center gap-3">
                    <TableIcon className="text-orange-500" size={24} />
                    <h2 className="text-xl font-black text-gray-800 uppercase tracking-tighter">Select Seat / Table</h2>
                </div>
                
                <div className="flex gap-4">
                    <div className="flex items-center gap-2">
                        <div className="w-3 h-3 rounded-full bg-green-500" />
                        <span className="text-xs font-bold text-gray-400 uppercase tracking-widest">Available</span>
                    </div>
                    <div className="flex items-center gap-2">
                        <div className="w-3 h-3 rounded-full bg-red-500" />
                        <span className="text-xs font-bold text-gray-400 uppercase tracking-widest">Occupied</span>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-4 md:grid-cols-6 gap-3">
                {tables.map((t) => {
                    const isOccupied = occupiedTables.includes(t)
                    return (
                        <button
                            key={t}
                            disabled={isOccupied}
                            onClick={next}
                            className={cn(
                                "relative group flex flex-col items-center justify-center p-6 rounded-3xl border-2 transition-all active:scale-90 overflow-hidden",
                                isOccupied 
                                    ? "bg-gray-50 border-gray-100 text-gray-300 cursor-not-allowed" 
                                    : "bg-white border-green-50 text-green-700 hover:border-green-500 hover:bg-green-50/50 shadow-sm shadow-green-50"
                            )}
                        >
                            <div className={cn(
                                "absolute top-2 right-2",
                                isOccupied ? "text-red-200" : "text-green-200 group-hover:text-green-500 transition-colors"
                            )}>
                                {isOccupied ? <XCircle size={14} /> : <CheckCircle2 size={14} />}
                            </div>
                            <span className="text-lg font-black">{t}</span>
                            <span className={cn(
                                "text-[10px] font-black uppercase tracking-tighter mt-1 px-2 py-0.5 rounded-full border",
                                isOccupied ? "bg-red-50 border-red-100 text-red-400" : "bg-green-50 border-green-100 text-green-600"
                            )}>
                                {isOccupied ? "Occupied" : "Available"}
                            </span>
                        </button>
                    )
                })}
            </div>
        </div>
    )
}
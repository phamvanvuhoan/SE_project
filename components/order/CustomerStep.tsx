import { User, Phone, ChevronRight } from "lucide-react"

interface CustomerStepProps {
    next: () => void
}

export default function CustomerStep({ next }: CustomerStepProps) {
    return (
        <div className="animate-in fade-in slide-in-from-bottom-4 duration-500">
            <h2 className="text-xl font-bold text-gray-800 mb-6">Customer Information</h2>

            <div className="space-y-4">
                <div className="relative group">
                    <User className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-orange-500 transition-colors" size={20} />
                    <input
                        placeholder="Customer Name"
                        className="w-full bg-gray-50 border-2 border-gray-50 p-4 pl-12 rounded-2xl focus:bg-white focus:border-orange-500 transition-all outline-none font-medium text-gray-800 placeholder:text-gray-400"
                    />
                </div>

                <div className="relative group">
                    <Phone className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-orange-500 transition-colors" size={20} />
                    <input
                        placeholder="Phone Number (Optional)"
                        className="w-full bg-gray-50 border-2 border-gray-50 p-4 pl-12 rounded-2xl focus:bg-white focus:border-orange-500 transition-all outline-none font-medium text-gray-800 placeholder:text-gray-400"
                    />
                </div>

                <button 
                    onClick={next} 
                    className="w-full bg-orange-500 text-white p-4 rounded-2xl font-bold shadow-lg shadow-orange-100 hover:bg-orange-600 active:scale-95 transition-all flex items-center justify-center gap-2 mt-6 group"
                >
                    Continue to Menu
                    <ChevronRight size={20} className="group-hover:translate-x-1 transition-transform" />
                </button>
            </div>
        </div>
    )
}
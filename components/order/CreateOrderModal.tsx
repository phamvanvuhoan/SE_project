"use client"

import { useState } from "react"
import OrderTypeStep from "./OrderTypeStep"
import TableSelectStep from "./TableSelectStep"
import CustomerStep from "./CustomerStep"
import MenuStep from "./MenuStep"
import { X } from "lucide-react"
import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs))
}

interface CreateOrderModalProps {
    onClose: () => void
}

export default function CreateOrderModal({ onClose }: CreateOrderModalProps) {
    const [step, setStep] = useState(0)

    const steps = [
        <OrderTypeStep key="order-type" next={() => setStep(1)} />,
        <TableSelectStep key="table-select" next={() => setStep(2)} />,
        <CustomerStep key="customer" next={() => setStep(3)} />,
        <MenuStep key="menu" next={onClose} />,
    ]

    const isMenuStep = step === 3

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-in fade-in duration-300">
            <div className={cn(
                "bg-white rounded-3xl shadow-2xl overflow-hidden relative animate-in zoom-in-95 duration-300 transition-all flex flex-col",
                isMenuStep ? "w-[98vw] h-[95vh] max-w-none" : "w-full max-w-2xl"
            )}>
                <div className={cn("flex flex-col h-full", !isMenuStep && "p-8")}>
                    <div className={cn("flex justify-between items-center", isMenuStep ? "px-8 pt-8 mb-4" : "mb-8")}>
                        <div>
                            <h2 className="text-2xl font-bold text-gray-800">Create New Order</h2>
                            <p className="text-gray-500 font-medium">Step {step + 1} of {steps.length}</p>
                        </div>
                        <button
                            onClick={onClose}
                            className={cn(
                                "w-10 h-10 flex items-center justify-center rounded-full bg-gray-50 text-gray-400 hover:text-red-500 hover:bg-red-50 transition-all shadow-sm",
                                isMenuStep && "bg-white"
                            )}
                        >
                            <X size={20} />
                        </button>
                    </div>

                    <div className={cn("flex-1 overflow-hidden", isMenuStep ? "h-full" : "min-h-[300px]")}>
                        {steps[step]}
                    </div>
                </div>

                {/* Progress Bar */}
                <div className="absolute bottom-0 left-0 h-1.5 w-full bg-gray-100">
                    <div 
                        className="h-full bg-orange-500 transition-all duration-500 ease-out"
                        style={{ width: `${((step + 1) / steps.length) * 100}%` }}
                    />
                </div>
            </div>
        </div>
    )
}
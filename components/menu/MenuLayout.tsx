interface MenuLayoutProps {
    left: React.ReactNode
    center: React.ReactNode
    right: React.ReactNode
}

export default function MenuLayout({ left, center, right }: MenuLayoutProps) {
    return (
        <div className="flex h-full bg-white divide-x divide-gray-100">
            {/* Sidebar Left */}
            <aside className="w-72 flex-shrink-0 overflow-y-auto">
                {left}
            </aside>

            {/* Main Content Center */}
            <main className="flex-1 overflow-y-auto bg-gray-50/50">
                {center}
            </main>

            {/* Right Panel/Cart */}
            <aside className="w-96 flex-shrink-0 overflow-y-auto bg-white">
                {right}
            </aside>
        </div>
    )
}
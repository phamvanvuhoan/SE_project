# Changelog

All notable changes to this project will be documented in this file.

## [2026-03-31] - Phase 2: Interactive Seats & Order Details

### Added
- **Smarts Stat-Colored Seats**: The seat selection screen now visually indicates table availability (**Green** = Free, **Red** = Occupied/In-use).
- **Interactive Order Details**: Dashboard order table rows are now clickable, opening a premium detail panel with full item breakdown and customer info.
- **Controlled Menu Editor**: Completely refactored the menu metadata editor to be reactive; metadata now populates and updates instantly as you navigate the inventory.
- **Enhanced Notifications**: Improved success feedback with branded toast messages for create/update actions.

### Fixed
- **Add Option Logic**: Resolved "abnormal" behavior in the Menu Management flow; new items are now correctly added to the grid and synced with the editor.
- **Clean Workspace**: Removed unused imports and variables in `app/menu/page.tsx` and `EditorSidebar.tsx` to reach 0 linting errors.

### Fixed
- **Dashboard Bugs**: Resolved 30+ linting errors and fixed missing component imports that were breaking the Overview page.
- **Menu Selection**: Fixed a layout bug where the menu selection was appearing "shrunken" or "empty" in the ordering modal.
- **Menu Editor**: Fixed a critical recursive bug in the `EditorSidebar` component that caused stack overflow.
- **Table Management**: Improved the `OrderTable` with status badges and consistent spacing.
- **Sidebar**: Corrected the sidebar links to use `next/link` for client-side navigation.

### Improved
- **Bill Management**: Enhanced the `BillSidebar` with item quantity adjustments and a clearer total amount display.
- **Step Process**: Improved the UI for the "New Order" multi-step flow (Order Type, Table, Customer Info).
- **Empty States**: Added helpful empty state messages for the Cart and Menu Selection.

---
*Maintained by Antigravity (AI Coding Assistant)*

Here is a list of all placeholder, empty, or unimplemented functions and event handlers in the project, categorized by file:

### 1. `app/dashboard/page.tsx`
* **Line 110: Empty `onAdd` Callback**
  ```tsx
  onAdd={() => {}}
  ```
  * *Context:* Passed to `<MenuGrid />` inside the "View Menu" modal. Clicking the "Add to Order" button on any menu card from the dashboard currently does nothing because the callback is empty.

---

### 2. `components/order/BillSidebar.tsx`
* **Line 61: Static "Cancel" Button Action**
  ```tsx
  <button className="flex-1 bg-gray-50 text-gray-400 py-3.5 rounded-2xl font-bold hover:bg-gray-100 transition-all active:scale-95">
      Cancel
  </button>
  ```
  * *Context:* The Cancel button lacks an `onClick` event handler, meaning clicking it has no effect (does not clear the current bill or close/reset state).

---

### 3. `components/order/CustomerStep.tsx`
* **Lines 15 & 23: Unbound Customer Information Inputs**
  ```tsx
  <input placeholder="Customer Name" ... />
  <input placeholder="Phone Number (Optional)" ... />
  ```
  * *Context:* The text inputs do not have standard state bindings (`value`, `onChange`). Clicking "Continue to Menu" simply moves to the next step via `next()` without capturing or storing the inputted details.

---

### 4. `components/order/MenuStep.tsx` & `CreateOrderModal.tsx`
* **Order Creation & Submission Handlers**
  ```tsx
  onSubmit={next} // In MenuStep.tsx, passed to BillSidebar
  ```
  * *Context:* The confirm order workflow passes `next` as the submit handler, which eventually resolves to `onClose` in `CreateOrderModal.tsx`. No order data is saved, dispatched, or persisted into any store or state.

---

### 5. `components/dashboard/OrderTable.tsx`
* **Line 255: Static "Print POS Receipt" Button**
  ```tsx
  <button className="w-full bg-orange-500 ...">
      Print POS Receipt
  </button>
  ```
  * *Context:* Inside the order details detail modal, this action button does not have an `onClick` event handler.
* **Line 259: Static "Send Digital Receipt via SMS" Button**
  ```tsx
  <button className="w-full bg-white text-gray-500 ...">
      Send Digital Receipt via SMS
  </button>
  ```
  * *Context:* This button lacks an `onClick` event handler.

---

### 6. `components/layout/Sidebar.tsx`
* **Line 72: Static "Settings" Button**
  ```tsx
  <button className="w-full flex items-center ...">
      Settings
  </button>
  ```
  * *Context:* The main navigation sidebar has a settings button with no `onClick` handler.
* **Line 76: Static "Logout" Button**
  ```tsx
  <button className="w-full flex items-center text-red-500 ...">
      Logout
  </button>
  ```
  * *Context:* The logout button has no `onClick` handler.
# Aqbah Inventory App

## Summary of Requirements and Goals

The Aqbah Inventory App was developed to provide a simple and efficient way for users to manage their inventory. The app enables users to securely log in, create accounts, add inventory items, track quantities, set reorder thresholds, and receive SMS alerts when stock levels are low. The primary user need addressed by this app is maintaining control over inventory levels and avoiding stockouts, which is especially valuable for small business owners or individuals managing personal stock.

## Screens and Features Supporting User Needs

To support these needs, the app included the following key screens:

- Login Screen: Allows users to log in or create new accounts.

- Inventory Screen: Displays a list of inventory items with options to update or delete them.

- Add Item Screen: Enables users to add new inventory items with details such as name, SKU, quantity, and threshold.

- SMS Permission Screen: Requests permission from the user to send SMS notifications when inventory is low.

These designs focused on a user-centered approach by keeping the interface clean, intuitive, and minimal, ensuring users could complete tasks with minimal effort. By using Material Design components and a simple layout, the app was successful in making inventory management straightforward and accessible.

## Coding Approach and Strategies

The coding process followed a modular approach where functionality was separated into different components, such as DatabaseHelper for persistence, InventoryAdapter for list display, and SmsUtils for messaging. Techniques such as RecyclerView adapters, SQLite database integration, and runtime permission handling were used to ensure the app worked reliably. These strategies, especially modularization and database-driven design, can be applied in future projects to simplify debugging, testing, and feature expansion.

## Testing for Functionality

Testing was conducted frequently using the Android Emulator. I verified each feature step by step: logging in, adding new items, updating quantities, deleting items, and triggering SMS alerts. This process was critical because it revealed small issues (such as database queries and adapter callbacks) that were resolved before finalization. Regular testing ensured that the app was functional, stable, and user-friendly.

## Innovation and Overcoming Challenges

One of the main challenges was handling SMS permission requests and low-inventory notifications in a seamless way that did not interrupt the user experience. To overcome this, I created a dedicated SmsPermissionActivity and linked it with the inventory update logic so that permission was requested automatically when needed. This required combining multiple Android features—permissions, SMS APIs, and activity navigation—in a creative way.

## Demonstration of Knowledge, Skills, and Experience

I was particularly successful in implementing the SQLite database integration and CRUD functionality (Create, Read, Update, Delete). This component demonstrated my ability to design persistent storage, connect it with UI elements, and ensure that inventory items were updated dynamically on the screen. Combining database operations with RecyclerView adapters highlighted my skills in both backend logic and frontend UI updates, making this a strong demonstration of my knowledge in mobile app development.

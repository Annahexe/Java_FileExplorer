# Java File Explorer

<p align="center">
  <img src="https://res.cloudinary.com/dwm2oprta/image/upload/v1769983051/fileExplorerThumbnail_kgwmlx.png" height="500" alt="FileExplorer thumbnail" />
</p>

A small desktop app made in **Java + Swing** that works like a **file manager + text editor**.

It lets you **browse folders**, **open files**, **edit text**, and perform common operations like **create / rename / copy / delete** — all from a simple GUI.

Built using a **MVC-style structure**:
- **Main** wires everything together
- **View** is the Swing UI (WindowBuilder)
- **Controller** handles interactions + state
- **Utilities** contains the file/folder logic (I/O)

---

## What it does

- Pick a folder (or start from the current directory)
- Navigate through folders (including **go to parent**)
- Select a file to **show its content**
- Toggle edit mode and do **search / replace**
- Save changes (overwrite / save-as)
- Manage files and folders directly from the app

---

## Cool features ✨

### 🗂️ File Explorer Actions
- **Open Path**: choose any folder and load its contents
- **Explore** selected folder / move forward
- **Back (`<`)**: go to the parent directory
- **Create** new folders & files

### ✍️ Built-in Text Editing
- **Show file content** inside a text pane
- **Edit toggle**: enable/disable editing
- **Search highlighting**: highlights matches in the editor
- **Replace** text quickly inside the file content
- **Save** (overwrite) or **Save as** (new file)

### 🧨 “Sudo” Confirmation for Dangerous Ops
For operations like **rename / delete / copy**, the app asks you to type **`sudo`** to confirm.
(It’s not real admin permissions—just a safety confirmation so you don’t click-delete by accident.)

### 📌 Info dialogs
- Quick info popups for files/folders (name, path, etc.)

---

## How to run

### In Eclipse
1. Open Eclipse
2. `File → Import → Existing Projects into Workspace`
3. Select the project folder
4. Run: `src/es/annahexe/Main.java` → **Run as Java Application**

### Requirements
- Java (JDK) 8+ recommended

---

## Project structure

- `Main.java`  
  Creates `View`, `Utilities`, and `Controller`.

- `View.java`  
  Swing UI components and getters for the controller.

- `Controller.java`  
  All listeners and logic connecting UI → Utilities.

- `Utilities.java`  
  File/folder operations: list, read, create, rename, copy, delete, save.

---

## ⚠️ Safety note

This app performs **real file operations** on your system:
- deleting a folder is **recursive**
- overwriting a file will replace its content

Use it in a safe folder while testing (like a sandbox directory).

---

## Licence 

This project is made for **learning / study purposes** and is **not intended for commercial use**.

**No warranty:** this software is provided “as is”.  
If it deletes/overwrites files or causes any damage/data loss, **I take no responsibility** — you use it at your own risk 😄

If you want to reuse parts of it, feel free to do so and keep it non-commercial.

---

Thanks for checking out my projects! 📂🫶

![VertexEngine Logo](doc/logo.png)

# VertexEngine

**VertexEngine** is an **editor-first 3D engine**, written in **Kotlin**, using **Compose Desktop** for tooling and **OpenGL** for rendering.

The project is currently **in active development** and focuses on building a solid, well-understood engine core from first principles, starting with a **scene graph–based editor**.

---

## 🎯 Vision

VertexEngine aims to be:

- **Editor-first** — tools are first-class citizens
- **Modular and explicit** — no hidden magic
- **Architecturally clean** — strict separation of concerns
- **Enjoyable to build** — small steps, visible results

This is **not** an attempt to replicate Unity or Unreal, but to design and understand a 3D engine core in a conscious, extensible way.

---

## 🧠 Core Principles

- Clear separation between:
  - Engine core (scene, components, math)
  - Rendering backend
  - Editor UI
- No UI dependencies inside the engine core
- Scene Graph as the primary world model
- Explicit, data-oriented APIs over implicit behavior

---

## ✨ Current Focus

The current development stage is centered around **Milestone 1: Mini Scene Graph + Editor**, which establishes the foundational editor and engine systems.

This milestone includes:
- A real-time 3D viewport
- A hierarchical scene graph
- Transform propagation
- A minimal component model
- Basic rendering
- Scene persistence (save/load)

You can follow the detailed progress and checklist here:  
👉 **[Milestone 1 — Mini Scene Graph + Editor](doc/MILESTONE_1.md)**

---

## 🚧 Work in Progress

VertexEngine is an evolving project. Many systems are intentionally kept minimal or deferred in order to avoid premature complexity.

The following areas are **explicitly out of scope for now**:
- Physics and collision detection
- Animation systems
- Audio
- Gameplay logic
- Scripting systems
- Advanced asset pipelines (materials, textures, glTF)

These will be addressed incrementally in future milestones once the core architecture is solid.

---

## 🏗️ Project Structure

```
engine/
├── core/
│   ├── math/
│   ├── scene/
│   ├── components/
│   └── serialization/
│
├── render/
│   ├── api/
│   └── opengl/
│
├── editor/
│   ├── ui/
│   ├── viewport/
│   ├── panels/
│   └── editor-state/
│
└── build.gradle.kts
```

---

## 🎯 Philosophy

> Build the engine you want to understand.

VertexEngine is about **learning deeply**, **designing consciously**, and **enjoying the process** of building a 3D engine from first principles.

---

## 📄 License

Open source — license to be defined (MIT or Apache 2.0 recommended).

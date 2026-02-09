## Milestone 1 — Mini Scene Graph + Editor

### Viewport 3D
- [x] Real-time rendering of a simple 3D scene (OpenGL)
- [ ] Editor camera
    - [ ] Orbit
    - [ ] Pan
    - [ ] Zoom (mouse)
- [ ] Optional ground grid
- [ ] XYZ world axes (visual gizmo)

---

### Scene Graph (Hierarchy)
- [ ] Hierarchy panel with node tree
- [ ] Create empty node
- [ ] Create primitive nodes
    - [ ] Cube
    - [ ] Sphere
    - [ ] Plane
- [ ] Rename node
- [ ] Reparent nodes (drag & drop)
- [ ] Duplicate node
- [ ] Delete node
- [ ] Node selection from hierarchy
- [ ] Optional viewport picking (raycast)

---

### Transform System
- [ ] Transform component per node
    - [ ] Position
    - [ ] Rotation
    - [ ] Scale
- [ ] Local transform calculation
- [ ] World transform propagation (parent → children)
- [ ] Inspector panel for numeric editing (XYZ)
- [ ] Transform reset actions
    - [ ] Reset position
    - [ ] Reset rotation
    - [ ] Reset scale

---

### Minimal Component Model
- [ ] Component base system
- [ ] MeshRenderer component
    - [ ] Primitive mesh support
    - [ ] Basic material
- [ ] Camera component
    - [ ] Editor camera
    - [ ] Optional scene camera
- [ ] Optional Light component

---

### Rendering
- [ ] Basic shading
    - [ ] Flat color
    - [ ] Lambert / Phong (simple)
- [ ] Selected object highlighting
- [ ] Optional wireframe mode

---

### Editor-First Architecture
- [ ] Modular project structure
    - [ ] `core` module
    - [ ] `render` module
    - [ ] `editor` module
- [ ] Clear separation of responsibilities
- [ ] Engine core has no UI dependencies

---

### Scene Persistence
- [ ] Save scene to JSON
- [ ] Load scene from JSON
- [ ] Serialize scene hierarchy
- [ ] Serialize transforms
- [ ] Serialize basic components
- [ ] Create new scene (reset state)

---

### Editor Ergonomics
- [ ] Keyboard shortcut: Delete node
- [ ] Keyboard shortcut: Duplicate node (`Ctrl / Cmd + D`)
- [ ] Keyboard shortcut: Save scene (`Ctrl / Cmd + S`)
- [ ] Scene dirty state tracking
- [ ] Optional exit confirmation when unsaved changes exist

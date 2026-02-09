## Milestone 1 — Mini Scene Graph + Editor (Execution Checklist)

> This checklist defines the **exact sequence of steps** to complete Milestone 1.
> Items marked as **optional** may be skipped without breaking the milestone.
> Items explicitly marked as **out of scope** belong to Milestone 2.

---

### Phase 1 — Renderable Scene Graph (Core Foundation)

- [x] Define `Scene` with a single `root: SceneNode`
- [x] Define `SceneNode`
  - `id`
  - `name`
  - `parent` (optional)
  - `children: MutableList<SceneNode>`
  - `transform: Transform`
  - `components: MutableList<Component>`
- [x] Implement `Transform`
  - Local `position`
  - Local `rotation`
  - Local `scale`
- [x] Implement local transform matrix calculation
- [x] Implement world transform propagation (parent → children)
- [x] Define `Component` base type
- [x] Implement `MeshRenderer` component
  - Supports `PrimitiveMesh.Cube`
  - References a basic `Material`
- [x] Implement minimal `Material`
  - Flat color
- [x] Replace demo renderer with a generic `SceneRenderer`
  - Accepts `Scene`
  - Traverses scene graph
  - Renders nodes with `MeshRenderer`

**Done when:**  
The editor renders a cube coming from a generic `Scene` and `SceneNode`, not from a demo-specific scene.

---

### Phase 2 — Editor Minimum Usable Tooling

#### Hierarchy
- [ ] Display scene hierarchy as a tree
- [ ] Select node from hierarchy
- [ ] Rename node
- [ ] Delete node
  - Toolbar button
  - `Delete` key
- [ ] Duplicate node (`Ctrl / Cmd + D`)
  - Deep copy (children + components)

#### Node Creation
- [ ] Create empty node
- [ ] Create cube primitive node

#### Inspector
- [ ] Show selected node properties
- [ ] Numeric editing of:
  - Position (XYZ)
  - Rotation (XYZ)
  - Scale (XYZ)
- [ ] Reset actions:
  - Reset position
  - Reset rotation
  - Reset scale

**Done when:**  
Nodes can be created, selected, edited, duplicated, and deleted, with live feedback in the viewport.

---

### Phase 3 — Viewport Navigation & Visual Aids

- [ ] Implement editor camera
  - Orbit
  - Zoom (mouse wheel)
  - Pan
- [ ] Optional ground grid
- [ ] XYZ world axes gizmo

**Done when:**  
The scene can be navigated comfortably and orientation is always clear.

---

### Phase 4 — Rendering Quality (Minimal)

- [ ] Flat shading (required)
- [ ] Lambert shading (optional)
- [ ] Selected object highlighting
- [ ] Wireframe mode (optional)

**Done when:**  
Selected objects are clearly distinguishable and rendering is visually readable.

---

### Phase 5 — Scene Persistence & Editor Ergonomics

#### Persistence
- [ ] Save scene to JSON
- [ ] Load scene from JSON
- [ ] Serialize scene hierarchy
- [ ] Serialize transforms
- [ ] Serialize `MeshRenderer` components
- [ ] Create new scene (reset editor state)

#### Ergonomics
- [ ] Save shortcut (`Ctrl / Cmd + S`)
- [ ] Scene dirty state tracking
- [ ] Optional exit confirmation on unsaved changes

**Done when:**  
A full workflow exists: create → edit → save → close → load → continue.

---

### Explicitly Out of Scope (Milestone 2)

- [ ] Drag & drop reparenting in hierarchy
- [ ] Viewport picking / raycasting selection
- [ ] Physics
- [ ] Animation systems
- [ ] Scripting

package com.example.physicsmate.model.notes;

import java.util.ArrayList;
import java.util.List;

public class NotesTopic {
    private final String name;
    private final List<NotesFormula> notesFormulas;
    private boolean expanded; // for expansion toggle

    public NotesTopic(String name) {
        this.name = name;
        this.notesFormulas = new ArrayList<>();
        this.expanded = false;
    }

    public String getName() { return name; }
    public List<NotesFormula> getFormulas() { return notesFormulas; }
    public void addFormula(NotesFormula f) { notesFormulas.add(f); }
    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
}

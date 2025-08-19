package com.example.physicsmate.model.notes;

public class NotesFormula
{
    public String title;
    public String latex;

    public NotesFormula(String title, String latex) {
        this.title = title;
        this.latex = latex;
    }

    public String getTitle() {
        return title;
    }

    public String getLatex() {
        return latex;
    }
}

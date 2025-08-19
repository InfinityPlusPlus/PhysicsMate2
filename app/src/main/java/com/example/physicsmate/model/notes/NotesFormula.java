package com.example.physicsmate.model.notes;

public class NotesFormula
{
    public String title;
    public String formulaEqn;
    public Boolean shouldConvertToTex = true;

    public NotesFormula(String title, String formulaEqn, Boolean shouldConvertToTex) {
        this.title = title;
        this.formulaEqn = formulaEqn;
        this.shouldConvertToTex = shouldConvertToTex;
    }

    public NotesFormula(String title, String formulaEqn) {
        this.title = title;
        this.formulaEqn = formulaEqn;
    }

    public String getTitle() {
        return title;
    }
    public String getFormulaEqn() {
        return formulaEqn;
    }
    public Boolean getShouldConvertToTex() {
        return shouldConvertToTex;
    }
}

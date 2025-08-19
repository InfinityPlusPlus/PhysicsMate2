package com.example.physicsmate.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.physicsmate.model.notes.NotesFormula;
import com.example.physicsmate.model.notes.NotesTopic;

import java.util.ArrayList;
import java.util.List;

public class NotesViewModel extends ViewModel {

    private final MutableLiveData<List<NotesTopic>> notesTopics;

    public NotesViewModel() {
        notesTopics = new MutableLiveData<>(new ArrayList<>());

        // Example preload data
        NotesTopic gravity = new NotesTopic("Gravity");
        gravity.addFormula(new NotesFormula(
                "Newton's Law of Gravitation",
                "F = G \\frac{m_1 m_2}{r^2}"
        ));
        gravity.addFormula(new NotesFormula(
                "Gravitational Potential Energy",
                "U = - G \\frac{m_1 m_2}{r}"
        ));

        NotesTopic em = new NotesTopic("Electricity & Magnetism");
        em.addFormula(new NotesFormula(
                "Coulomb's Law",
                "F = k \\frac{q_1 q_2}{r^2}"
        ));
        em.addFormula(new NotesFormula(
                "Gauss's Law",
                "\\oint E \\cdot dA = \\frac{q_{enc}}{\\epsilon_0}"
        ));

        List<NotesTopic> list = new ArrayList<>();
        list.add(gravity);
        list.add(em);

        notesTopics.setValue(list);
    }

    public LiveData<List<NotesTopic>> getNotesTopics() {
        return notesTopics;
    }

    // Add a new topic
    public void addTopic(NotesTopic topic) {
        List<NotesTopic> current = notesTopics.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(topic);
        notesTopics.setValue(current);
    }

    // Add a new formula to an existing topic
    public void addFormula(String topicName, NotesFormula formula) {
        List<NotesTopic> current = notesTopics.getValue();
        if (current == null) return;
        for (NotesTopic t : current) {
            if (t.getName().equalsIgnoreCase(topicName)) {
                t.addFormula(formula);
                break;
            }
        }
        notesTopics.setValue(current); // trigger UI update
    }
}

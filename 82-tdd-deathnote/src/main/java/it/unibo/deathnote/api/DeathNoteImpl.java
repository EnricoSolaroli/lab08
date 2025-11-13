package it.unibo.deathnote.api;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * implementation of DeathNote class.
 */
public class DeathNoteImpl implements DeathNote {

    private static final int TIME_LIMIT_TO_KILL = 40;
    private static final int TIME_LIMIT_TO_DETAILS = 6040;
    private final Map<String, List<String>> deathNote = new HashMap<>(); 
    private long writeNameStart;
    private long deathTime;
    private String current;
    /**
     * Returns the rule with the given number.
     *
     * @param ruleNumber the number of the rule to return. The first rule has number one
     * @return the rule with the given number
     * @throws IllegalArgumentException if the given rule number is smaller than 1 or larger
     *     than the number of rules
     */

    @Override
    public String getRule(final int ruleNumber) {
        if (ruleNumber < 1 || ruleNumber > RULES.size()) {
            throw new IllegalArgumentException("Numero regola non valido: " + ruleNumber);
        }
        return RULES.get(ruleNumber - 1);
    }

    /**
     * The human whose name is written in this DeathNote will die.
     *
     * @param name the name of the human to kill
     * @throws NullPointerException if the given name is null.
     */
    @Override
    public void writeName(final String name) {
       if (name.isEmpty()) {
            throw new NullPointerException(); //NOPMD
       } else {
            deathNote.put(name, new LinkedList<>(Arrays.asList("", "")));
            current = name;
       }
       writeNameStart = System.currentTimeMillis();
       deathTime = writeNameStart;
    }

    /**
     * If the cause of death is written within the next 40 milliseconds of writing the person's
     * name, it will happen.
     *
     * @param cause the cause of the human's death
     * @return true if the cause was written within 40 milliseconds, false otherwise
     * @throws IllegalStateException if there is no name written in this DeathNote,
     *     or the cause is null
     */
    @Override
    public boolean writeDeathCause(final String cause) {
        deathTime = System.currentTimeMillis();
        if (deathNote.isEmpty() || cause.isEmpty()) {
            throw new IllegalStateException();
        }
        if ((deathTime - writeNameStart) < TIME_LIMIT_TO_KILL) {
            final List<String> lista = deathNote.get(current);
            lista.set(0, cause);
            return true;
        } else {
            return false;
        }
    }

    /**
     * After writing the cause of death, details of the death should be written in the next
     * 6 seconds and 40 milliseconds.
     *
     * @param details the details of the human's death
     * @return true if the details were written within 6 seconds and 40 milliseconds, false otherwise
     * @throws IllegalStateException if there is no name written in this DeathNote,
     *     or the details are null
     */
    @Override
    public boolean writeDetails(final String details) {
        final long detailsTime = System.currentTimeMillis();
        if (deathNote.isEmpty() || details.isEmpty()) {
            throw new IllegalStateException();
        }
        if ((detailsTime - deathTime) < TIME_LIMIT_TO_DETAILS) {
            final List<String> lista = deathNote.get(current);
            lista.set(1, details);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Provides the cause of death of the person with the given name.
     *
     * @param name the name of the person whose death cause to return
     * @return the death cause of the person with the given name.
     *     If the cause of death is not specified, the method will return "heart attack".
     * @throws IllegalArgumentException if the provider name is not written in this DeathNote
     */
    @Override
    public String getDeathCause(final String name) {
        if (isNameWritten(name)) {
            return deathNote.get(name).get(0).isEmpty() ? "heart attack" : deathNote.get(name).get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Provides the details of the death of the person with the given name.
     *
     * @param name the name of the person whose death cause to return
     * @return the death details of the person with the given name,
     *     or an empty string if no details have been provided.
     * @throws IllegalArgumentException if the provider name is not written in this DeathNote.
     */
    @Override
    public String getDeathDetails(final String name) {
       if (isNameWritten(name)) {
            return deathNote.get(name).get(1);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if the given name is written in this DeathNote.
     *
     * @param name the name of the person
     * @return true if the given name is written in this DeathNote, false otherwise
     */
    @Override
    public boolean isNameWritten(final String name) {
        return !(name.isEmpty() || !deathNote.containsKey(name));
    }
}

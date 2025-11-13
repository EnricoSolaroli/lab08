package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.api.DeathNoteImpl;

class TestDeathNote {

    private static final String PERSON_1 = "Matteo";
    private static final String PERSON_2 = "Gianfranco";
    private static final String CAUSE_OF_DEATH_1 = "omicidio";
    private static final String CAUSE_OF_DEATH_2 = "karting accident";
    private static final String DEFOULT_DEATH = "heart attack";
    private static final String DEATH_DETAIL_1 = "ran for too long";
    private static final String DEATH_DETAIL_2 = "not eat enough";
    private static final int TIME_TO_SLEEP_1 = 40;
    private static final int TIME_TO_SLEEP_2 = 6100;
    private DeathNoteImpl deathNote;

    @BeforeEach
    void setUp() {
        this.deathNote = new DeathNoteImpl();
    }

    @Test
    void checkNumberRules() {
        final int[] array = {-1, 0}; 
        for (final int i : array) {
            assertThrowsExactly(
                IllegalArgumentException.class,
                new Executable() { 
                    @Override
                    public void execute() throws Throwable {
                        deathNote.getRule(i);
                    }
                }
            );
        }
    }

    @Test
    void checkRules() {
        for (int i = 1; i < DeathNote.RULES.size(); i++) {
            final String rule = DeathNote.RULES.get(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    @Test
    void checkPersonDie() {
        assertFalse(deathNote.isNameWritten(PERSON_1));
        deathNote.writeName(PERSON_1);
        assertTrue(deathNote.isNameWritten(PERSON_1));
        assertFalse(deathNote.isNameWritten(PERSON_2));
        assertFalse(deathNote.isNameWritten("")); 
    }

    @Test
    void checkCauseOfDeath() throws InterruptedException {
        assertThrowsExactly(
            IllegalStateException.class, 
            new Executable() {
                @Override
                public void execute() throws Throwable {
                    deathNote.writeDeathCause(CAUSE_OF_DEATH_1);
                }
            } 
        );
        deathNote.writeName(PERSON_1);
        assertEquals(deathNote.getDeathCause(PERSON_1), DEFOULT_DEATH);
        deathNote.writeName(PERSON_2);
        deathNote.writeDeathCause(CAUSE_OF_DEATH_2);
        assertEquals(deathNote.getDeathCause(PERSON_2), CAUSE_OF_DEATH_2);
        Thread.sleep(100);
        assertFalse(deathNote.writeDeathCause(CAUSE_OF_DEATH_1));
        assertEquals(deathNote.getDeathCause(PERSON_2), CAUSE_OF_DEATH_2);
    }

    @Test
    void checkDeathDetails() throws InterruptedException {
         assertThrowsExactly(
            IllegalStateException.class, 
            new Executable() {
                @Override
                public void execute() throws Throwable {
                    deathNote.writeDetails(DEATH_DETAIL_2);
                }
            } 
        );
        deathNote.writeName(PERSON_1);
        assertTrue(deathNote.getDeathDetails(PERSON_1).isEmpty());
        Thread.sleep(TIME_TO_SLEEP_1);
        deathNote.writeDetails(DEATH_DETAIL_1);
        assertEquals(deathNote.getDeathDetails(PERSON_1), DEATH_DETAIL_1);
        deathNote.writeName(PERSON_2);
        Thread.sleep(TIME_TO_SLEEP_2);
        assertFalse(deathNote.writeDetails(DEATH_DETAIL_2));
        assertNotEquals(deathNote.getDeathDetails(PERSON_2), DEATH_DETAIL_1);
    }
}



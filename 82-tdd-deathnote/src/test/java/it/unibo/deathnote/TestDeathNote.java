package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.function.Executable;
import java.lang.Thread;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.api.DeathNoteImpl;

class TestDeathNote {

    private DeathNoteImpl deathNote;
    private final String person1 = "Matteo";
    private final String person2 = "Gianfranco";
    private final String causeOfDeath1 = "omicidio";
    private final String causeOfDeath2 = "karting accident";
    private final String defoultDeth = "heart attack";
    private final String deathDetail1 = "ran for too long";
    private final String deathDetail2 = "not eat enough";

    void setUp() {
        this.deathNote = new DeathNoteImpl();
    }

    @Test
    void checkNumberRules() {
        int[] array = new int[] {0, -1}; 
        for (int i : array) {
            try {
                deathNote.getRule(i);
                Assertions.fail("the rule -1 should give an exception, but it doesn't");
            } catch (IllegalArgumentException e) {
                assertNotNull(e.getMessage());
                assertFalse(e.getMessage().isBlank());
            } 
        }
    }

    @Test
    void checkRules() {
        for(int i=1; i<=DeathNote.RULES.size(); i++) {
            final String rule = DeathNote.RULES.get(i);
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }        
    }

    @Test
    void checkPersonDie() {
        assertFalse(deathNote.isNameWritten(person1));
        deathNote.writeName(person1);
        assertTrue(deathNote.isNameWritten(person1));
        assertFalse(deathNote.isNameWritten(person2));
        assertFalse(deathNote.isNameWritten(new String()));
    }

    @Test
    void checkCauseOfDeath() throws InterruptedException {
        assertThrowsExactly(
            IllegalStateException.class, 
            new Executable() {
                @Override
                public void execute() throws Throwable {
                    deathNote.writeDeathCause(causeOfDeath1);    
                }   
            } 
        );
        deathNote.writeName(person1);
        assertTrue(deathNote.getDeathCause(person1).equals(defoultDeth));
        deathNote.writeName(person2);
        deathNote.writeDeathCause(causeOfDeath2);
        assertTrue(deathNote.getDeathCause(person2).equals(causeOfDeath2));
        Thread.sleep(100);
        assertFalse(deathNote.writeDeathCause(causeOfDeath1));
        assertTrue(deathNote.getDeathCause(person2).equals(causeOfDeath2));
    }

    @Test
    void checkDeathDetails() throws InterruptedException {
         assertThrowsExactly(
            IllegalStateException.class, 
            new Executable() {
                @Override
                public void execute() throws Throwable {
                    deathNote.writeDetails(deathDetail2);
                }   
            } 
        );
        deathNote.writeName(person1);
        assertTrue(deathNote.getDeathDetails(person1).isEmpty());
        deathNote.writeDetails(deathDetail1);
        assertEquals(deathNote.getDeathDetails(person1), deathDetail1);
        deathNote.writeName(person2);
        Thread.sleep(6100);
        assertFalse(deathNote.writeDetails(deathDetail2));
        assertTrue(deathNote.getDeathDetails(person2).equals(deathDetail1));
    }
}



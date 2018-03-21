package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_SHAW;

import org.junit.Test;

import seedu.address.logic.commands.EditCommand.EditCinemaDescriptor;
import seedu.address.testutil.EditCinemaDescriptorBuilder;

public class EditCinemaDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditCinemaDescriptor descriptorWithSameValues = new EditCinemaDescriptor(DESC_GV);
        assertTrue(DESC_GV.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_GV.equals(DESC_GV));

        // null -> returns false
        assertFalse(DESC_GV.equals(null));

        // different types -> returns false
        assertFalse(DESC_GV.equals(5));

        // different values -> returns false
        assertFalse(DESC_GV.equals(DESC_SHAW));

        // different name -> returns false
        EditCinemaDescriptor editedGv = new EditCinemaDescriptorBuilder(DESC_GV).withName(VALID_NAME_SHAW).build();
        assertFalse(DESC_GV.equals(editedGv));

        // different phone -> returns false
        editedGv = new EditCinemaDescriptorBuilder(DESC_GV).withPhone(VALID_PHONE_SHAW).build();
        assertFalse(DESC_GV.equals(editedGv));

        // different email -> returns false
        editedGv = new EditCinemaDescriptorBuilder(DESC_GV).withEmail(VALID_EMAIL_SHAW).build();
        assertFalse(DESC_GV.equals(editedGv));

        // different address -> returns false
        editedGv = new EditCinemaDescriptorBuilder(DESC_GV).withAddress(VALID_ADDRESS_SHAW).build();
        assertFalse(DESC_GV.equals(editedGv));

    }
}

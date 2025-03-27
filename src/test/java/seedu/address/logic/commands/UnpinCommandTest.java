package seedu.address.logic.commands;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Pin;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UnpinCommand}.
 */
public class UnpinCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndex_unpinsPerson() throws Exception {
        // Pre-pin the person
        Person originalPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person pinnedPerson = new Person(
                originalPerson.getName(),
                originalPerson.getPhone(),
                originalPerson.getEmail(),
                originalPerson.getAddress(),
                originalPerson.getNote(),
                originalPerson.getTags(),
                new Pin(true)
        );
        model.setPerson(originalPerson, pinnedPerson);

        UnpinCommand unpinCommand = new UnpinCommand(INDEX_FIRST_PERSON);
        CommandResult result = unpinCommand.execute(model);

        Person unpinnedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertTrue(!unpinnedPerson.getPin().isPinned());
        assertEquals(String.format(UnpinCommand.MESSAGE_UNPIN_PERSON_SUCCESS, INDEX_FIRST_PERSON.getOneBased()),
                result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        UnpinCommand unpinCommand = new UnpinCommand(outOfBoundIndex);

        assertThrows(CommandException.class, () -> unpinCommand.execute(model),
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void unpinPerson_delegatesToAddressBook() {
        Model model = new ModelManager();
        Person person = new PersonBuilder().withPin(true).build();
        model.addPerson(person);

        Person unpinned = new PersonBuilder(person).withPin(false).build();
        model.setPerson(person, unpinned);

        model.unpinPerson(unpinned);
        assertFalse(model.getFilteredPersonList().get(0).getPin().isPinned());
    }


    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}

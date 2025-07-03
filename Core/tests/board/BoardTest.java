package board;

import static org.junit.Assert.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import controller.execution.parameter.ParametersContextProvider;
import game.Game;
import other.GameLoader;
import other.concept.Concept;
import util.DefaultParameter;

/**
 * Test suite focused on the concept Board.
 * 
 */
@ExtendWith(ParametersContextProvider.class)
public class BoardTest {

	
	/**
     * Checks that the line length defined in the game is less than or equal to
     * the maximum dimension of one side of the board.
     * <p>
     * It also verifies that the "line" concept is active within the game's concept set.
     * The test fails if the concept is missing or if the line length exceeds the board dimension.
     * </p>
     * 
     * @param gameName  The name of the game to load and test.
     * @param lineLength The length of the line to check, defaulting to 3 if not specified.
     * @throws AssertionError if the "line" concept is not present or the line length is too long.
     */
    @TestTemplate
    @Tag("Static")
	public void lineLessOrEqualThanBoardSide(String gameName, 
			@DefaultParameter("3") int lineLength) {
    	
        // GAME LOADING
		Game game = GameLoader.loadGameFromName(gameName);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
		
		/// VERIFY THERE IS THE LINE LUDEME
		boolean lineConcept = concepts.get(Concept.Line.id());
		if (!lineConcept) {
			fail("Line concept is not present");
		}
		
		/// RETRIEVE THE LENTGH OF ONE SIDE OF THE BOARD
		int side = game.board().graph().maxDim();
		
        assertTrue(lineLength <= side, "Line should be less than or equal to board side");
	}
	
}


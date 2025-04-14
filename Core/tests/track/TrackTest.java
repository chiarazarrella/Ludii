package track;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import annotation.DefaultParameter;
import game.Game;
import game.equipment.container.board.Board;
import game.equipment.container.board.Track;
import game.equipment.container.board.Track.Elem;
import game.types.board.SiteType;
import game.util.graph.GraphElement;
import other.GameLoader;
import other.concept.Concept;
import parameterResolver.UserInputTestProvider;

@ExtendWith(UserInputTestProvider.class)
public class TrackTest {
	
	/**
	 * @param gameName
	 * @param owner
	 * @param size
	 * 
	 * to run the test w/o the dynamic test framework
	 * @ParameterizedTest
	 * @CsvSource({"20 Squares.lud, 1, 15"})
	 */
	@TestTemplate
	@Tag("Static")
	public void sizeOfTrack(String gameName, 
			@DefaultParameter("1") String owner, @DefaultParameter("3") int size) {
		
		 // GAME LOADING
		Game game = GameLoader.loadGameFromName(gameName);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
		
		boolean trackConcept = concepts.get(Concept.Track.id());
		if (!trackConcept) {
			fail("Track concept is not present");
		}
		
		// INDEX OF THE BOARD
		List<? extends GraphElement> cells = game.board().graph().elements(SiteType.Cell);
		List<Integer> cellIds = new ArrayList<>();
		for (GraphElement cell : cells) {
			cellIds.add(cell.id());
		}
		
		// TRACK TO CONSIDER BASED ON THE OWNER
		List<Track> tracks = game.board().tracks();
		int ownerIdx = Integer.parseInt(owner);
		Track track = null;
		for (Track t : tracks) {
			if (t.owner() == ownerIdx) {
				track = t;
				break;
			}
		}
		
		// ACTUAL LENGTH OF THE TRACK ON THE BOARD
		int counter = 0;
		for (Elem e : track.elems()) {
		    
			if (cellIds.contains(e.site)) {
				counter++;
			} 
			
		}
		
		assertTrue(String.format("The length of the track is %d", counter), size == counter);
		
	}

}

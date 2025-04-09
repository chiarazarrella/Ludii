package track;

import static org.junit.Assert.fail;

import java.util.BitSet;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;

import game.Game;
import game.equipment.container.board.Board;
import game.equipment.container.board.Track;
import other.GameLoader;
import other.concept.Concept;
import parameterResolver.UserInputTestProvider;

@ExtendWith(UserInputTestProvider.class)
public class TrackTest {
	
	
	public void sizeTrack(String gameName, int size) {
		
		 // GAME LOADING
		Game game = GameLoader.loadGameFromName(gameName);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
		
		boolean lineConcept = concepts.get(Concept.Track.id());
		if (!lineConcept) {
			System.out.println("Line concept is NOT present");
			fail("Line concept is not present");
		}
		
		Board board = game.board();
		List<Track> tracks = board.tracks();
		
		for(Track t: tracks) {
		
		}
		
	}

}

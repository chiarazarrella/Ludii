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
import org.junit.jupiter.params.provider.ValueSource;

import controller.execution.parameter.ParametersContextProvider;
import game.Game;
import game.equipment.container.board.Track;
import game.equipment.container.board.Track.Elem;
import game.types.board.SiteType;
import game.util.graph.GraphElement;
import other.GameLoader;
import other.concept.Concept;
import other.context.Context;
import other.trial.Trial;
import util.DefaultParameter;

@ExtendWith(ParametersContextProvider.class)
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
	//@ParameterizedTest
	//@CsvSource({"58 Holes.lud, 30"})
	public void sizeOfTrack(String gameName, 
			int owner, @DefaultParameter("3") int size) {
		
		 // GAME LOADING
		Game game = GameLoader.loadGameFromName(gameName);
		
		// CONCEPTS LOADING
		BitSet concepts = game.computeBooleanConcepts();
	
		
		boolean trackConcept = concepts.get(Concept.Track.id());
		if (!trackConcept) {
			fail("Track concept is not present");
		}
		
		boolean edgeConcept = concepts.get(Concept.Edge.id());
		
		boolean vertexConcept = concepts.get(Concept.Vertex.id());
		
		boolean cellConcept = concepts.get(Concept.Cell.id());
		
		SiteType type = null;
		
		if(edgeConcept) {
			type = SiteType.Edge;
		}
		
		if(vertexConcept) {
			type = SiteType.Vertex;
		}
		
		if(cellConcept) {
			type = SiteType.Cell;
		}
		
		Context context = null;
		List<? extends GraphElement> sites = null;
		List<Track> tracks = null;
		if(game.hasSubgames()) {
			
			context = new Context(game, new Trial(game));
	  		game.start(context);
	  		sites = context.board().graph().elements(type);	  
	  		tracks = context.board().tracks();
		}else {
			
			sites = game.board().graph().elements(type);
			tracks = game.board().tracks();
		}
		
		List<Integer> siteIds = new ArrayList<>();
		for (GraphElement site : sites) {
			siteIds.add(site.id());
		}
		
		// TRACK TO CONSIDER BASED ON THE OWNER
		
		//System.out.println(tracks.size());
		Track track = null;
		for (Track t : tracks) {
			if (t.owner() == owner) {
				track = t;
				break;
			}
		}
		
		if(track == null) {
			fail(String.format("The owner %d does not have a track", owner));
		}
		
		// ACTUAL LENGTH OF THE TRACK ON THE BOARD
		int counter = 0;
		for (Elem e : track.elems()) {
		    
			if (siteIds.contains(e.site)) {
				counter++;
			} 
			
		}
		
		assertTrue(String.format("The length of the track is %d", counter), size == counter);
		
	}
		
	
	/*@TestTemplate
	@Tag("Dynamic")
	public void dynTestTrack(String gameName) {
		assert (true);
	}*/

}

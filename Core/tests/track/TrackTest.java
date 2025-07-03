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

/**
 * Test suite for validating the properties of Tracks in various games.
 *
 */
@ExtendWith(ParametersContextProvider.class)
public class TrackTest {
	
	/**
     * Validates the length of the track owned by a given owner in a specified game.
     * <p>
     * The test performs the following steps:
     * <ol>
     *   <li>Loads the game specified by the game name.</li>
     *   <li>Checks if the 'Track' concept is enabled for the game.</li>
     *   <li>Determines the site type relevant for tracks (Edge, Vertex, or Cell).</li>
     *   <li>Obtains the current board context and all graph elements of the identified site type.</li>
     *   <li>Locates the track owned by the specified owner.</li>
     *   <li>Counts the number of track elements that are valid sites on the board.</li>
     *   <li>Asserts that the counted length equals the expected size parameter.</li>
     * </ol>
     * </p>
     *
     * @param gameName The name of the game to load and test.
     * @param owner The identifier of the track owner to check.
     * @param size The expected size (length) of the track.
     * @throws AssertionError if the track concept is missing, the owner does not have a track, or the track length does not match the expected size.
     */
    @TestTemplate
    @Tag("Static")
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
		
	
}

import junit.framework.TestCase;
import java.util.*;


public class LastFMRecommenderTest extends TestCase
{
	public static LastFMRecommender LFMR;

	// test all 5 methods (listFriends, commonFriends, listArtists, listTop10, recommend10)
	public static void test_methods()
	{
		LFMR = new LastFMRecommender();
                LFMR.load_artists("artists.dat");
                LFMR.load_user_artists("user_artists.dat");
                LFMR.load_user_friends("user_friends.dat");
		ArrayList<Integer> target_1 = new ArrayList<Integer>(Arrays.asList(275, 428, 515, 761, 831, 909, 1209, 1210, 1230, 1327, 1585, 1625, 1869));
		ArrayList<Integer> target_2 = new ArrayList<Integer>(Arrays.asList(255, 837, 1740, 1801));
		ArrayList<Integer> target_3 = new ArrayList<Integer>(Arrays.asList(51, 53, 64, 65, 70, 72, 77));
		ArrayList<Integer> target_4 = new ArrayList<Integer>(Arrays.asList(289, 72, 89, 292, 498, 67, 288, 701, 227, 300));
		ArrayList<Integer> target_5 = new ArrayList<Integer>(Arrays.asList(72, 82, 30, 1713, 3768, 1001, 65, 1014, 880, 10621));


		assertTrue(LFMR.listFriends(2).equals(target_1));
		assertTrue(LFMR.commonFriends(3, 78).equals(target_2));
		assertTrue(LFMR.listArtists(2, 4).equals(target_3));
		assertTrue(LFMR.listTop10().equals(target_4));
		assertTrue(LFMR.recommend10(4).equals(target_5));
	}

		
}

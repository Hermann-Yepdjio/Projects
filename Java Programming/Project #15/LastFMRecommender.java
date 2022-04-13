import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class LastFMRecommender
{
	TreeMap<Integer,TreeMap<Integer,Integer>> user_artists;
	TreeMap<Integer, Set<Integer>> user_friends;
	TreeMap<Integer, String> artists;
	BufferedReader reader;

	public LastFMRecommender()
	{
		user_artists = new TreeMap<Integer,TreeMap<Integer,Integer>>() ;
		user_friends = new TreeMap<Integer, Set<Integer>>();
		artists = new TreeMap<Integer, String>();
	}

	public void load_artists(String file_name)
	{
		try
		{
			reader = new BufferedReader(new FileReader(file_name));
			String line = reader.readLine(); // skip the first line in the file
			line = reader.readLine();
			while (line != null) 
			{
				String[] tokens = line.split("\t");
				artists.put(Integer.parseInt(tokens[0]), tokens[1]);
				line = reader.readLine(); // read next line
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load_user_artists(String file_name)
        {
                try
                {
                        reader = new BufferedReader(new FileReader(file_name));
                        String line = reader.readLine(); // skip the first line in the file
                        line = reader.readLine();
                        while (line != null)
                        {
                                String[] tokens = line.split("\t");
				if (!user_artists.containsKey(Integer.parseInt(tokens[0])))
					user_artists.put(Integer.parseInt(tokens[0]), new TreeMap<Integer,Integer>());
				user_artists.get(Integer.parseInt(tokens[0])).put(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
	
                                line = reader.readLine(); // read next line
                        }
                        reader.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

	public void load_user_friends(String file_name)
        {
                try
                {
                        reader = new BufferedReader(new FileReader(file_name));
                        String line = reader.readLine(); // skip the first line in the file
                        line = reader.readLine();
                        while (line != null)
                        {
                                String[] tokens = line.split("\t");
				if (!user_friends.containsKey(Integer.parseInt(tokens[0])))
                                        user_friends.put(Integer.parseInt(tokens[0]), new TreeSet<Integer>());
                                user_friends.get(Integer.parseInt(tokens[0])).add(Integer.parseInt(tokens[1]));
                                                              
			       	line = reader.readLine(); // read next line
                        }
                        reader.close();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

	public ArrayList<Integer> listFriends(int user)
	{
		Set<Integer> friends_list = user_friends.get(user);
		System.out.println("\n\nFriends of user " + user + " :\n");
		for (Integer friend_id : friends_list)
			System.out.println(friend_id);
		return new ArrayList<Integer>(friends_list);
	}

	public ArrayList<Integer> commonFriends(int user1, int user2)
        {
                Set<Integer> friends_list_1 = user_friends.get(user1);
		Set<Integer> friends_list_2 = user_friends.get(user2);
		friends_list_1.retainAll(friends_list_2);
		System.out.println("\n\nCommon friends of users " + user1 + " and " + user2 + " :\n");
                for (Integer friend_id : friends_list_1)
                        System.out.println(friend_id);
		return new ArrayList<Integer>(friends_list_1);
        }

	public ArrayList<Integer> listArtists(int user1, int user2)
        {
                TreeMap<Integer, Integer> artists_list_1 = user_artists.get(user1);
                TreeMap<Integer, Integer> artists_list_2 = user_artists.get(user2);
                artists_list_1.keySet().retainAll(artists_list_2.keySet());
		System.out.println("\n\nCommon artists listened by users " + user1 + " and " + user2 + " :\n");
		for (Map.Entry<Integer, Integer> artist : artists_list_1.entrySet())
                        System.out.println(artist.getKey() + " " + artists.get(artist.getKey()));
		return new ArrayList<Integer>(artists_list_1.keySet());
	}

	//sort tree map in reverse order
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) 
	{
	    Comparator<K> valueComparator = new Comparator<K>() 
	    {
		    public int compare(K k1, K k2) 
		    {
			    int compare = map.get(k1).compareTo(map.get(k2));
			    if (compare == 0)
				    return 1;
			    else
				    return -compare;
		    }
	    };
	    Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}

	public ArrayList<Integer> listTop10()
        {
		TreeMap<Integer, Integer> artists_listening_count = new TreeMap<Integer, Integer>();
		for (Map.Entry<Integer, TreeMap<Integer, Integer>> user: user_artists.entrySet())
		{
			TreeMap<Integer, Integer> artists = user.getValue();
			for (Map.Entry<Integer, Integer> artist : artists.entrySet())
			{
				if(!artists_listening_count.containsKey(artist.getKey()))
					artists_listening_count.put(artist.getKey(), 0);
				artists_listening_count.put(artist.getKey(), artists_listening_count.get(artist.getKey()) + artist.getValue());
			}        		
		}

		Map sortedMap = sortByValues(artists_listening_count);	
		Set set = sortedMap.entrySet();
		Iterator i = set.iterator();

		int count = 0;
		ArrayList<Integer> top_10 = new ArrayList<Integer>();
		System.out.println("\n\nTop 10 artists listened by all users:\n");
                while(i.hasNext() && count < 10)
                {
                        Map.Entry me = (Map.Entry)i.next();
                        System.out.print(me.getKey() + "  " + artists.get(me.getKey()) + ": ");
                        System.out.println(me.getValue());
			top_10.add((int)me.getKey());
			count++;
                }
		return top_10;
        }

	public ArrayList<Integer> recommend10(int user)
	{
		ArrayList<Integer> friends_list = listFriends(user);
		System.out.println("\n\n\n\nThe recommended 10 most popular artists listened by " + user + " and his/her friends are: \n\n");
		friends_list.add(user);
		TreeMap<Integer, Integer> artists_listening_count = new TreeMap<Integer, Integer>();
		for (int user_id : friends_list)
		{
			TreeMap<Integer, Integer> artists = user_artists.get(user_id);
			for (Map.Entry<Integer, Integer> artist : artists.entrySet())
			{
				if(!artists_listening_count.containsKey(artist.getKey()))
					artists_listening_count.put(artist.getKey(), 0);
				artists_listening_count.put(artist.getKey(), artists_listening_count.get(artist.getKey()) + artist.getValue());
			}
		}

		Map sortedMap = sortByValues(artists_listening_count);
                Set set = sortedMap.entrySet();
                Iterator i = set.iterator();

                int count = 0;
                ArrayList<Integer> top_10 = new ArrayList<Integer>();
                while(i.hasNext() && count < 10)
                {
                        Map.Entry me = (Map.Entry)i.next();
                        System.out.print(me.getKey() + "  " + artists.get(me.getKey()) + ": ");
                        System.out.println(me.getValue());
                        top_10.add((int)me.getKey());
                        count++;
                }
                return top_10;

	}




	public static void main(String[] args)
    	{
		LastFMRecommender LFMR = new LastFMRecommender();
		LFMR.load_artists("artists.dat");
		LFMR.load_user_artists("user_artists.dat");
		LFMR.load_user_friends("user_friends.dat");
		
		LFMR.listFriends(2);
		LFMR.commonFriends(3, 78);
		LFMR.listArtists(2, 4);
		LFMR.listTop10();
		LFMR.recommend10(4);
		
    	}

}

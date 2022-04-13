import java.util.*;
import java.io.*;


public class Main 
{
	//read the csv file and return an array of movies
	public static List<Movie> read_csv(String file_name)
	{
		List<Movie> movies = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file_name))) 
		{
		    String line;
		    line = br.readLine(); //  To skip the first line
		    while ((line = br.readLine()) != null) 
		    {
				String[] values = line.split(",");
			if (values.length != 0 )
			{
				//if(values[0].length() == 0)
					//values[0] = "0";
				if (values[12].length() == 0)
					values[12] = "0";
				if(values[3].length() == 0)
					values[3] = "0";
			
				Movie entry = new Movie(Integer.parseInt(values[0]), values[1], values[2], Integer.parseInt(values[3]), values[4], values[5], values[6], values[7], values[8], values[9], values[10], values[11], Integer.parseInt(values[12]), Double.parseDouble(values[13]));

				movies.add(entry);
			}
		    }
		}
	       	catch (IOException e) 
		{
			e.printStackTrace();
    		}

		return movies;

	}


	//get search details from the user
	public static String[] get_user_input()
	{
		String[] inputs = {"", "", "", ""};
		// Using Scanner for Getting Input from User
        	Scanner in = new Scanner(System.in);
		System.out.print("Year: ");
		inputs[0] = in.nextLine();
		System.out.print("Score: ");
                inputs[1] = in.nextLine();
		System.out.print("Language: ");
                inputs[2] = in.nextLine();
		System.out.print("Rating: ");
                inputs[3] = in.nextLine();

		return inputs;
	}

	//read the csv file, create the hash table of red black trees, and return it
	public static Hashtable<String, RedBlackBST> create_hashtable()
	{
		List<Movie> movies = read_csv("movies.csv");
		Hashtable<String, RedBlackBST> ht = new Hashtable<String, RedBlackBST>(); 
		RedBlackBST<Integer, HashSet<Movie>> RBT_year = new RedBlackBST<Integer, HashSet<Movie>>();
		RedBlackBST<Double, HashSet<Movie>> RBT_imdb_score = new RedBlackBST<Double, HashSet<Movie>>();
		RedBlackBST<String, HashSet<Movie>> RBT_content_rating = new RedBlackBST<String, HashSet<Movie>>();
		RedBlackBST<String, HashSet<Movie>> RBT_language = new RedBlackBST<String, HashSet<Movie>>();

		for (Movie entry: movies)
		{
			HashSet<Movie> tmp = RBT_year.get(entry.year);
			if (tmp != null)
				tmp.add(entry);
			else
			{
				 HashSet<Movie> tmp_2 = new HashSet<Movie>();
				 tmp_2.add(entry);
				 RBT_year.put(entry.year, tmp_2);
			}

			tmp = RBT_imdb_score.get(entry.imdb_score);
                        if (tmp != null)
                                tmp.add(entry);
                        else
                        {
                                 HashSet<Movie> tmp_2 = new HashSet<Movie>();
                                 tmp_2.add(entry);
                                 RBT_imdb_score.put(entry.imdb_score, tmp_2);
                        }

			tmp = RBT_content_rating.get(entry.content_rating);
                        if (tmp != null)
                                tmp.add(entry);
                        else
                        {
                                 HashSet<Movie> tmp_2 = new HashSet<Movie>();
                                 tmp_2.add(entry);
                                 RBT_content_rating.put(entry.content_rating, tmp_2);
                        }

			tmp = RBT_language.get(entry.language);
                        if (tmp != null)
                                tmp.add(entry);
                        else
                        {
                                 HashSet<Movie> tmp_2 = new HashSet<Movie>();
                                 tmp_2.add(entry);
                                 RBT_language.put(entry.language, tmp_2);
                        }


		}
		ht.put("year", RBT_year);
		ht.put("imdb_score", RBT_imdb_score);
		ht.put("content_rating", RBT_content_rating);
		ht.put("language", RBT_language);

		return ht;
	}

	//get search details from the user, perform the searh, and return the movies that were found
	public static HashSet<Movie> get_user_input_and_search()
	{
		Hashtable<String, RedBlackBST> ht = create_hashtable();
		String[] inputs = get_user_input();
		HashSet<Movie> results = new HashSet<Movie>();
		boolean initialized = false;
		System.out.print("\n\n\nresults (Movies -> ");
		for (int i = 0; i < inputs.length; i++)
		{
			if(!inputs[i].equals("") && !inputs[i].equals("-") && i == 0)
			{
				System.out.print("year:" + inputs[i]);
				@SuppressWarnings("unchecked")
                		RedBlackBST<Integer, HashSet<Movie>> RBT_year = (RedBlackBST<Integer, HashSet<Movie>>) ht.get("year");
                		results = RBT_year.get(Integer.parseInt(inputs[i]));
				initialized = true;
			}

			if(!inputs[i].equals("") && !inputs[i].equals("-") && i == 1)
                        {
				System.out.print(" score:" + inputs[i]);
                                @SuppressWarnings("unchecked")
                                RedBlackBST<Double, HashSet<Movie>> RBT_imdb_score = (RedBlackBST<Double, HashSet<Movie>>) ht.get("imdb_score");
                                if(!initialized)
				{
                                        results = RBT_imdb_score.get(Double.parseDouble(inputs[i]));
					initialized = true;
				}
                                else if (results.size() !=0)
                                        results.retainAll(RBT_imdb_score.get(Double.parseDouble(inputs[i])));
                        }

			if(!inputs[i].equals("") && !inputs[i].equals("-") && i == 2)
                        {
                                System.out.print(" language:" + inputs[i]);
                                @SuppressWarnings("unchecked")
                                RedBlackBST<String, HashSet<Movie>> RBT_language = (RedBlackBST<String, HashSet<Movie>>) ht.get("language");
                                if(!initialized)
                                {
                                        results = RBT_language.get(inputs[i]);
                                        initialized = true;
                                }
                                else if (results.size() !=0)
                                        results.retainAll(RBT_language.get(inputs[i]));
                        }


			if(!inputs[i].equals("") && !inputs[i].equals("-") && i == 3)
                        {
				System.out.print(" rating:" + inputs[i]);
                                @SuppressWarnings("unchecked")
                                RedBlackBST<String, HashSet<Movie>> RBT_content_rating = (RedBlackBST<String, HashSet<Movie>>) ht.get("content_rating");
                                if(!initialized)
				{
                                        results = RBT_content_rating.get(inputs[i]);
					initialized = true;
				}
				else if (results.size() !=0)
                                        results.retainAll(RBT_content_rating.get(inputs[i]));
                        }

			

		}

		System.out.println(")\n");
		if(results!= null && results.size() > 0)
		{
			System.out.print("\n\n[");
			List<Movie> list = new ArrayList<Movie>(results);
			for(int i = 0; i < list.size() - 1; i++)
				System.out.print(list.get(i).id + ", ");
			System.out.println(list.get(list.size() - 1).id + "]\n\n--------------------------------------------------------\n\n");
		}



		return results;
                
	}


	public static void main(String[] args) 
	{
				HashSet<Movie> results = get_user_input_and_search();

		if(results!= null && results.size() > 0)
                {
			for (Movie entry : results)
			{
				entry.print_movie();
				System.out.println("\n\n----------------------------------------------\n\n\n-----------------------------------------------\n\n");
			}
		}

	}
}

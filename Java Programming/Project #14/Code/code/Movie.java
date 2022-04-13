public class Movie
{
	// Instance Variables
	int id;
	String Color;
	String movie_title;
	int duration;
	String director_name;
	String actor_1_name;
	String actor_2_name;
	String actor_3_name;
	String movie_imdb_link;
	String language;
	String country;
	String content_rating;
	int year;
	double imdb_score;

   	 // Constructor Declaration of Class
    	public Movie(int id, String Color, String movie_title, int duration, String director_name, String actor_1_name, String actor_2_name, String actor_3_name, String movie_imdb_link, String language, String country, String content_rating, int year, double imdb_score) 
	{
		this.id = id;
		this.Color = Color;
		this.movie_title = movie_title;
		this.duration = duration;
		this.director_name = director_name;
		this.actor_1_name = actor_1_name;
		this.actor_2_name = actor_2_name;
		this.actor_3_name = actor_3_name;
                this.movie_imdb_link = movie_imdb_link;
                this.language = language;
                this.country = country;
                this.content_rating = content_rating;
                this.year = year;
                this.imdb_score = imdb_score;

	}

	public void print_movie()
	{
		System.out.println("id:" + this.id + "\ncolor:" + this.Color + "\ntitle:" + this.movie_title + "\nduration:" + this.duration + "\ndirector_name:" + this.director_name + "\nact1:" + this.actor_1_name + "\nact2:" + this.actor_2_name + "\nact3:" + this.actor_3_name + "\nmovie_imdb_link:" + this.movie_imdb_link + "\nlanguage:" + this.language + "\ncountry:" + this.country + "\ncontent_rating:" + this.content_rating + "\ntitle_year:" + this.year + "\nimdb_score:" + this.imdb_score);
	}
}


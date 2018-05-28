public class Participant {

	String name;
	int guessedPlayer = 0;
	Integer correctGuesses = 0;
	
	public Participant()
	{
	     name = "";
	}
	public Participant(String nameGiven)
	{
	     name = nameGiven;
	}

	public int getCorrectGuesses(){
	    return correctGuesses;
	}
	public String getName(){
	    return name;
	}
	
	public int getGuessedPlayer()
	{
		return guessedPlayer;
	}
	public void setName(String nameGiven){
		
		name = nameGiven;
		
	}
	public void setGuessedPlayer(Integer intGiven)
	{
		guessedPlayer = intGiven;
	}
	public void correctGuess(){
		
		correctGuesses++;
	}
	public String getInfo()
	{
		return name + " " + correctGuesses.toString() + " ";
	}

}

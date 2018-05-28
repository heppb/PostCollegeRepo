import java.util.*;
import java.sql.*;
import org.jibble.pircbot.*;

public class TwitchBot extends PircBot implements Runnable {
	
	protected boolean isBetting = false;
	//ArrayList<String> usersBetting = new ArrayList<String>(0); 
	//ArrayList<Integer> usersBetInfo = new ArrayList<Integer>(0);
	ArrayList<Participant> leaderboard = new ArrayList<Participant>(0);
	ArrayList<Participant> compareList = new ArrayList<Participant>(0);
	public Timers betTimer = new Timers();
	public boolean isTimerRunning = false;

	public TwitchBot() {
		this.setName("your_bot");
		
		this.isConnected();
		
	}
	
	public static Comparator<Participant> COMPARE_BY_CORRECT = new Comparator<Participant>() {  
		  
		public int compare(Participant one, Participant other) 
		  {
	          return one.correctGuesses.compareTo(other.correctGuesses);
	      }
	  };
	
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		
		if(message.equalsIgnoreCase("!kilroy")) {
			sendMessage(channel, "Ban Kilroy from the Stream Room!");
		}
		if(message.equalsIgnoreCase("!bot")) {
			sendMessage(channel, "This is Your_Channel's chat bot. V2.0");
		}
		if(message.equalsIgnoreCase("!music")) {
			
			sendMessage(channel, "Music Bot in progress. Coming soon!");
		};
		if(message.equalsIgnoreCase("!startbets") && sender.equalsIgnoreCase("your_channel") && isTimerRunning == false)
		{
			isBetting = true;
			startBets(channel);
			Thread t = new Thread(new Runnable() {
			    public void run() {
			    	System.out.println("Thread is running inside StartBets");
					betTimer.run();
					System.out.println("Thread has run");
					betTimer.cancel();
					isTimerRunning = false;
					if(isBetting == true) //checks to send message, in case the end bets command was done.
					{
						sendMessage(channel, "Betting has now ended. Results will happen after the match");
					}
					isBetting = false;
					System.out.println("Thread has cancelled");
					betResults(channel);
			    }
			});
			t.start();
			isTimerRunning = true;
		}
		if(message.equalsIgnoreCase("!endbets") && sender.equalsIgnoreCase("your_channel"))
		{
			sendMessage(channel, "Betting has been closed"); //timer might still run in background.
			isBetting = false;
		}
		if((message.equalsIgnoreCase("!win1") || message.equalsIgnoreCase("!win2")) && sender.equalsIgnoreCase("third_chair"))
		{
			checkBets(channel, message);
		}
		if((message.equalsIgnoreCase("1") || message.equalsIgnoreCase("2")) && isBetting == true)
		{
			addToBets(channel, message, sender);
		}
		if(message.equals("!standings"))
		{
			standings(channel);
		}
		if(message.equalsIgnoreCase("!clearStandings") && sender.equalsIgnoreCase("your_channel"))
		{
			clearStandings(channel);
		}
		
	}
	public void startBets(String channel)
	{
		sendMessage(channel, "Betting has started. Type 1 for P1, Type 2 for P2. Betting will end in 30 seconds.");
	}
	public void addToBets(String channel, String message, String sender)
	{
		for(int x = 0; x < leaderboard.size(); x++)
		{
			String temp = leaderboard.get(x).getName();
			if(temp.equalsIgnoreCase(sender))
			{
				if(message.equals("1"))
				{
					leaderboard.get(x).setGuessedPlayer(1);
					//sendMessage(channel, "Bet Recorded 1");
					return;
				}
				else
				{
					leaderboard.get(x).setGuessedPlayer(2);
					//sendMessage(channel, "Bet Recorded 2");
					return;
				}
			}
		}
		leaderboard.add(new Participant(sender));
		if(message.equals("1"))
		{
			leaderboard.get(leaderboard.size()-1).setGuessedPlayer(1);
			//sendMessage(channel, "Bet Recorded 3");
			return;
		}
		else
		{
			leaderboard.get(leaderboard.size()-1).setGuessedPlayer(2);
			//sendMessage(channel, "Bet Recorded 4");
			return;
		}
		
	}
	public void checkBets(String channel, String message)
	{
		String output = "Congrats to ";
		int checkInt;
		if(message.equals("!win1"))
		{
			checkInt = 1;
		}
		else
		{
			checkInt = 2;
		}
		
		for(int x = 0; x < leaderboard.size(); x++)
		{
			int tempInt = leaderboard.get(x).getGuessedPlayer();
			if(checkInt == tempInt)
			{
				leaderboard.get(x).correctGuess();
				output = output + leaderboard.get(x).getName() + " ";
			}
		}
		
		sendMessage(channel, output);
	}
	
	public void standings(String channel) {
		
		if(leaderboard.isEmpty())
		{
			sendMessage(channel, "Standings are Empty");
			return;
		}
		String temp = "";
		compareList = leaderboard;
		Collections.sort(compareList, COMPARE_BY_CORRECT.reversed());
		for(int i=0;i<compareList.size();i++){
		    temp = temp + compareList.get(i).getInfo() + "\\n";
		} 
		sendMessage(channel, "The current Leaderboard is " + temp);
	}
	
	public void betResults(String channel)
	{
		Integer guess1 = 0;
		Integer guess2 = 0;
		Double guessPercent;
		Double guessPercent2;
		if(leaderboard.isEmpty())
		{
			return;
		}
		else
		{
			for(int x = 0; x < leaderboard.size(); x++)
			{
				if(leaderboard.get(x).getGuessedPlayer() == 1)
				{
					guess1++;
				}
				else
				{
					guess2++;
				}
			}
			Integer guessTotal = (guess1 + guess2);
			if(guess1 != 0)
			{
				guessPercent = (double) (guess1/guessTotal) * 100;
			}
			else
			{
				guessPercent = (double) 0;
			}
			if(guess2 != 0)
			{
				guessPercent2 = (double) (guess2/guessTotal) * 100;
			}
			else
			{
				guessPercent2 = (double) 0;
			}
			sendMessage(channel, "The current bets are P1: " + guess1.toString() +  " (" + guessPercent + "%) " + "And P2: " + guess2.toString() +  " (" + guessPercent2 + "%) ");
		}
	}
	
	public void clearStandings(String channel)
	{
		leaderboard.clear();
		sendMessage(channel, "The standings have been cleared");
	}
	@Override
	public void run() {
		System.out.println("Thread is running outside startbets. How did we get here?");
	}
	
	//Greeter Bot but nobody likes to use these anymore.
	
	/*protected void onJoin(String channel, String sender, String login, String hostname) {
		
		if(!(sender.equalsIgnoreCase("your_channel_bot") || sender.equalsIgnoreCase("your_channel"))) {
			sendMessage(channel, "Welcome to the Channel " + sender + "!");
		}
		else {
			sendMessage(channel, "Testing");
		}
		
	}; */
	
	//Database Name Collector?
	
	protected void onJoin(String channel, String sender, String login, String hostname) {
		
		if(!(sender.equalsIgnoreCase("your_channel_bot") || sender.equalsIgnoreCase("your_channel"))) {
			sendMessage(channel, "Welcome to the Channel " + sender + "!");
			try
		    {
		      // create a mysql database connection
		      //String myDriver = "org.gjt.mm.mysql.Driver";
			  String myDriver = "com.mysql.cj.jdbc.Driver";
		      String myUrl = "jdbc:mysql://localhost:3306/your_database_name";
		      Class.forName(myDriver).newInstance();
		      Connection conn = DriverManager.getConnection(myUrl, "root", "Your_Password");

		      // the mysql insert statement
		      String query = " insert into twitch_table (twitch_name)"
		        + " values (?)";

		      // create the mysql insert preparedstatement
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setString (1, sender);

		      // execute the preparedstatement
		      preparedStmt.execute();
		      
		      conn.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception!");
		      System.err.println(e.getMessage());
		    }
		}
		else {
			sendMessage(channel, "Testing");
		}
		
	}; 
	
}

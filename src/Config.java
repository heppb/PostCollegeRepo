
public class Config {
	
	public static String channel = "#your_channel";
	
	public static void main(String[] args) throws Exception{
	
		TwitchBot bot = new TwitchBot();
		bot.setVerbose(true);
		bot.connect("irc.chat.twitch.tv", 6667, "your_twitch_bot_auth_code");
		bot.joinChannel(channel);
		bot.getName();
		bot.setMessageDelay(1000);
	}

}

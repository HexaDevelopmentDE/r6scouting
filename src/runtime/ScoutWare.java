package runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Tool for Player Scouting for Rainbow Six Siege
 * @author Konstantin Koark
 * @category Scouting
 * @category Rainbow Six Siege
 * @version 1.0.0
 */
public class ScoutWare {
	//--------------------------variables---------------------------------
	/**list of links to the users*/
	private ArrayList<String> linkList = new ArrayList<String>();
	
	//--------------------------constructor-------------------------------
	/**initialize PlayerData*/
	public ScoutWare() {
		output( " ____   __     ____  _        _         _____           _ \r\n"
				+ "|  _ \\ / /_   / ___|| |_ __ _| |_ ___  |_   _|__   ___ | |\r\n"
				+ "| |_) | '_ \\  \\___ \\| __/ _` | __/ __|   | |/ _ \\ / _ \\| |\r\n"
				+ "|  _ <| (_) |  ___) | || (_| | |_\\__ \\   | | (_) | (_) | |\r\n"
				+ "|_| \\_\\\\___/  |____/ \\__\\__,_|\\__|___/   |_|\\___/ \\___/|_|" 
				+ "\n"
				+ "Scouting Tool for Rainbow Six Siege\n"
				+ "by HexaDevelopmentDE"
				+ "\n\n");
	}
	
	//--------------------------methods-----------------------------------
	/**
	 * add a player to list
	 * @param username
	 * @throws IOException 
	 */
	public void addPlayer(String username) throws IOException {
		String userID = getUbisoftID(username);
		if(userID != null) {
			linkList.add("https://r6stats.com/de/stats/" + userID + "/");
			output(username + " added to List");
		}else {
			output("Error - User " + username + " not found");
		}
		
	}
	/**
	 * get ubisoft id of the player
	 * @param username
	 * @return
	 * @throws IOException
	 */
	private String getUbisoftID(String username) throws IOException {
		try {
			//open reader --> return a json file
			URL url = new URL("https://r6stats.com/api/player-search/" + username.toLowerCase() + "/pc");
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla");
			connection.setReadTimeout(6000);
			connection.setConnectTimeout(6000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			StringBuilder text = new StringBuilder();
		    int cp;
		    while ((cp = reader.read()) != -1) {
		      text.append((char) cp);
		    }
		    
		    reader.close();
		    
		    JSONObject json = new JSONObject(text.toString());
		    
		    //get ubisoft id from json file
			String content = json.get("data").toString();
			return content.substring((content.length()-39), (content.length()-3));
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * fetch player data
	 */
	public void getPlayerData() throws IOException {
		output("\n" + "---Getting User Data---\n" + "Username \t" + "Level \t" + "K/D \t" + "Rank \t\t" + "Main-Operator \t");
		for(int k = 0; k < linkList.size(); k++) {
			//connect to website
			String url = linkList.get(k);
			Document docStats = Jsoup.connect(url)
					.timeout(6000)
					.get(); 
			Document docOps = Jsoup.connect(url + "operators")
					.timeout(6000)
					.get(); 
			
			//get data
			Elements name = docStats.select(".player-info__player__username");
			Elements quick_stats = docStats.select(".quick-stat__value");
			Elements rank = docStats.select(".top-region__info__rank");
			Elements stats = docStats.select(".stats-card-item__value");
			Elements ops = docOps.select(".operator-meta__name");
			
			//format data
			String output = null;
			String operator = ops.text();
			output = name.text() + "\t";
			output = output + quick_stats.text().substring(4, 7) + "\t";
			output = output + stats.text().substring(0, 4) + "\t";
			output = output + rank.text() + "\t";
			char[] opsList = operator.toCharArray();
			int counter = 1;
			operator = "";
			for(int i = 0; i < opsList.length; i++) {
				operator = operator + opsList[i];
				if(opsList[i] == ' ') {
					counter++;
				}
				if(counter > 10) {
					break;
				}
			}
			output = output + operator.substring(0, operator.length()-1) + "\t";
			
			
			
			//output
			output(output);
		}
	}
	
	/**
	 * ouput (output can be configured as needed)
	 * @param output
	 */
	private void output(String output) {
		System.out.println(output);
	}
}

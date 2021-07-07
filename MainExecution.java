package runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author konstantin koark
 * @version 1.0.0
 */
public class MainExecution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ScoutWare scouting = new ScoutWare();
		
		try {
			System.out.println("Enter usernames [username1 username2 ...]: ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			char[] input = reader.readLine().toCharArray();
			List<String> user = new ArrayList<String>();
			
			String temp = "";
			for(int i = 0; i < input.length; i++) {
				if(input[i] != ' ') {	
					temp = temp + input[i];
				}else {
					user.add(temp);
					temp = "";
				}
			}
			user.add(temp);
			
			System.out.println();
			for(int i = 0; i < user.size(); i++) {
				scouting.addPlayer(user.get(i));
			}
			
			scouting.getPlayerData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}

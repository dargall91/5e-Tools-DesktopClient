package encounter;

import org.json.simple.JSONObject;

public class PlayerData {
	private int players;
	private int level;
	
	public PlayerData(JSONObject json) {
		try {
			players = (int) (long) json.get("players");
			level = (int) (long) json.get("level");
		} catch (Exception e) {
			System.out.println("Error in PlayerData(json): " + e.getMessage());
		}
	}
	
	public PlayerData(int players, int level) {
		this.players = players;
		this.level = level;
	}
	
	public PlayerData() {
		players = 1;
		level = 1;
	}
	
	public int getPlayers() {
		return players;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setPlayers(int players) {
		this.players = players;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public String toString() {
		return "players: " + players + " level: " + level;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("players", players);
			obj.put("level", level);
		} catch (Exception e) {
			System.out.println("Error in encounter.PlayerData.toJson: " + e.getMessage());
		}
		
		return obj;
	}
}

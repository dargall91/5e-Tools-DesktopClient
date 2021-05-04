package encounter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Encounter {
	private String name;
	private ArrayList<MonsterData> monsterData;
	private ArrayList<PlayerData> playerData;
	private String difficulty, theme;
	private int xpTotal;
	private boolean lairAction;
	private final int[] EASY = { 25, 50, 75, 125, 250, 300, 350, 450, 550,
		600, 800, 1000, 1100, 1250, 1400, 1600, 2000, 2100, 2400, 2800 };
	private final int[] MEDIUM = { 50, 100, 150, 250, 500, 600, 750, 900,
		1100, 1200, 1600, 2000, 2200, 2500, 2800, 3200, 3900, 4200, 4900, 5700 };
	private final int[] HARD = { 75, 150, 225, 375, 750, 900, 1100, 1400,
		 1600, 1900, 2400, 3000, 3400, 3800, 4300, 4800, 5900, 6300, 7300, 8500 };
	private final int[] DEADLY = { 100, 200, 400, 500, 1100, 1400, 1700,
		 2100, 2400, 2800, 3600, 4500, 5100, 5700, 6400, 7200, 8800, 9500, 10900, 12700 };
		 private final int[] BUDGET = { 300, 600, 1200, 1700, 3500, 4000,
			 5000, 6000, 7500, 9000, 10500, 11500, 13500, 15000, 18000, 20000, 25000, 27000, 30000, 40000 };
	
	public Encounter() {
		playerData = new ArrayList<PlayerData>();
		monsterData = new ArrayList<MonsterData>();
		xpTotal = 0;

		FileReader reader = null;
		
		try {
			//InputStream in = this.getClass().getClassLoader().getResourceAsStream("EmptyData/NewEncounter.json");
			reader = new FileReader("EmptyData/NewEncounter.json", Charset.forName("UTF-8"));

			//if (in == null)
            //		in = new FileInputStream(new File("EmptyData/NewEncounter.json"));

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(reader);
            		
			//JSONObject json = new JSONObject(new JSONTokener(in));
			initFromJson(json);
		} catch (Exception e) {
			System.out.println("Error in Encounter(): " + e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Encounter(String name) {
		FileReader reader = null;

		try {
			//InputStream in = this.getClass().getClassLoader().getResourceAsStream("Encounters/" + name + ".json");
			reader = new FileReader("Encounters/" + name + ".json", Charset.forName("UTF-8"));

			//if (in == null)
            //		in = new FileInputStream(new File("Encounters/" + name + ".json"));

			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(reader);
            		
			//JSONObject json = new JSONObject(new JSONTokener(in));
			initFromJson(json);
		} catch (Exception e) {
			System.out.println("Error in Encounter(String name): " + e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Encounter(JSONObject json) {
		initFromJson(json);
	}
	
	private void initFromJson(JSONObject json) {
		playerData = new ArrayList<PlayerData>();
		monsterData = new ArrayList<MonsterData>();
		
		try {
			name = (String) json.get("name");
			theme = (String) json.get("theme");
			lairAction = (boolean) json.get("lairAction");
			JSONArray arr = (JSONArray) json.get("playerData");
			
			for (int i = 0; i < arr.size(); i++)
				playerData.add(new PlayerData((JSONObject) arr.get(i)));

			arr = (JSONArray) json.get("monsterData");
			
			for (int i = 0; i < arr.size(); i++)
				monsterData.add(new MonsterData((JSONObject) arr.get(i)));
		} catch (Exception e) {
			System.out.println("Error in Encounter.initFromJson(JSONObject): " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<PlayerData> getPlayerData() {
		return playerData;
	}
	
	public ArrayList<MonsterData> getMonsterData() {
		return monsterData;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTheme(String theme) {
		this.theme = theme;
	}

	public void setLairAction(boolean lairAction) {
		this.lairAction = lairAction;
	}

	public boolean hasLairAction() {
		return lairAction;
	}
	
	public void addPlayerData() {
		playerData.add(new PlayerData());
	}
	
	public void updateNumPlayers(int index, int players) {
		playerData.get(index).setPlayers(players);
	}
	
	public void updatePlayerLevel(int index, int level) {
		playerData.get(index).setLevel(level);
	}
	
	public void deletePlayerData(int index) {
		playerData.remove(index);
	}
	
	public void addMonsterData(String monster, int xp) {
		monsterData.add(new MonsterData(monster, xp));
	}
	
	public void deleteMonsterData(int index) {
		monsterData.remove(index);
	}
	
	public void updateMonsterQuantity(int index, int quantity) {
		if (quantity == 0)
			deleteMonsterData(index);
		
		else
			monsterData.get(index).setQuantity(quantity);
	}
	
	//TODO: may not be needed
	public boolean containsMonster(String name) {
		for (MonsterData i : monsterData)
			if (i.getMonster().equals(name))
				return true;
				
		return false;
	}
	
	/**
	 * Returns the index of the specified monster, or -1 if not found
	 */
	public int getMonsterIndex(String name) {
		int index = 0;
		for (MonsterData i : monsterData) {
			if (i.getMonster().equals(name))
				return index;
			
			index++;
		}
				
		return -1;
	}
	
	public void setMinion(int index, boolean minion) {
		monsterData.get(index).setMinion(minion);
	}
	
	public void setReinforcement(int index, boolean reinforcement) {
		monsterData.get(index).setReinforcement(reinforcement);
	}
	
	public void setMonsterXP(int index, int xp) {
		monsterData.get(index).setXP(xp);
	}
	
	public void setInitiative(int index, int initiative) {
		monsterData.get(index).setInitiative(initiative);
	}
	
	public int getEasyThreshold() {
		int threshold = 0;
		
		for (PlayerData i : playerData)
			threshold += i.getPlayers() * EASY[i.getLevel() - 1];
			
		return threshold;
	}
	
	public int getMediumThreshold() {
		int threshold = 0;
		
		for (PlayerData i : playerData)
			threshold += i.getPlayers() * MEDIUM[i.getLevel() - 1];
			
		return threshold;
	}
	
	public int getHardThreshold() {
		int threshold = 0;
		
		for (PlayerData i : playerData)
			threshold += i.getPlayers() * HARD[i.getLevel() - 1];
			
		return threshold;
	}
	
	public int getDeadlyThreshold() {
		int threshold = 0;
		
		for (PlayerData i : playerData)
			threshold += i.getPlayers() * DEADLY[i.getLevel() - 1];
			
		return threshold;
	}
	
	public int getDailyBudget() {
		int budget = 0;
		
		for (PlayerData i : playerData)
			budget += i.getPlayers() * BUDGET[i.getLevel() - 1];
			
		return budget;
	}
	
	public int getXPTotal() {
		calcDifficulty();
		return xpTotal;
	}
	
	public String getDifficulty() {
		calcDifficulty();
		return difficulty;
	}
	
	/**
	 * Cannot be used for calcDifficulty because this does not exclude minions, used for combat tracker
	 * may not even be needed depending on final implementation
	 */
	public int getMonsterTotal() {
		int numMonsters = 0;
		
		for (MonsterData i : monsterData)
			numMonsters += i.getQuantity();
		
		return numMonsters;
	}
	
	private void calcDifficulty() {
		int numMonsters = 0;
		xpTotal = 0;
		
		for (MonsterData i : monsterData) {
			if (!i.isMinion() && i.getXP() != 0) {
				numMonsters += i.getQuantity();
				xpTotal += i.getXP() * i.getQuantity();
			}
		}		
		
		if (numMonsters == 2)
			xpTotal = xpTotal * 3 / 2;
			
		else if (numMonsters >= 3 && numMonsters <= 6)
			xpTotal = xpTotal * 2;
		
		else if (numMonsters >= 7 && numMonsters <= 10)
			xpTotal = xpTotal * 5 / 2;
			
		else if (numMonsters >= 11 && numMonsters <= 14)
			xpTotal = xpTotal * 3;
			
		else if (numMonsters >= 15)
			xpTotal = xpTotal * 4;
			
		if (xpTotal < getEasyThreshold())
			difficulty = "Trivial";
		
		else if (xpTotal >= getEasyThreshold() && xpTotal < getMediumThreshold())
			difficulty = "Easy";
		
		else if (xpTotal >= getMediumThreshold() && xpTotal < getHardThreshold())
			difficulty = "Medium";
			
		else if (xpTotal >= getHardThreshold() && xpTotal < getDeadlyThreshold())
			difficulty = "Hard";
			
		else
			difficulty = "Deadly";
	}
	
	public String getTheme() {
		return theme;
	}
	
	public int getTotalPlayers() {
		int numPlayers = 0;
		
		for (PlayerData i : playerData)
			numPlayers += i.getPlayers();
			
		return numPlayers;
	}
	
	public String toJsonString() {
		String result = "{}";
		
		try {
			result = toJson().toString();
		} catch (Exception e) {
			System.out.println("Error in Encounter.toJsonString: " + e.getMessage());
		}
		
		return result;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		JSONArray dataArr = new JSONArray();
		JSONArray monArr = new JSONArray();
		
		try {
			obj.put("name", name);
			obj.put("theme", theme);
			obj.put("lairAction", lairAction);
			
			for (PlayerData i : playerData)
				dataArr.add(i.toJson());
			
			obj.put("playerData", dataArr);
			
			for (MonsterData i : monsterData)
				monArr.add(i.toJson());
			
			obj.put("monsterData", monArr);
		}catch (Exception e) {
			System.out.println("Error in Encounter.toJson: " + e.getMessage());
		}
		
		return obj;
	}
	
	public boolean saveJson() {
		boolean result = false;
		
		try {
			File file = new File("Encounters/" + name + ".json");
			
			if (!file.exists())
				file.createNewFile();
				
			FileWriter out = new FileWriter(file);
			out.write(toJsonString());
			out.flush();
			out.close();
			result = true;
			
			System.out.println("Encounter " + name + " save successful.");
		} catch (Exception e) {
			System.out.println("Error in Encounter.saveJson: " + e.getMessage());
		}
		
		return result;
	}
}

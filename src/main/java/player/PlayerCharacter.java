package player;

import org.json.simple.JSONObject;

public class PlayerCharacter {
	private String name;
	private int bonus, ac;

	public PlayerCharacter() {
		name = "Name";
		bonus = 0;
		ac = 0;
	}
	
	public PlayerCharacter(String name) {
		this.name = name;
		bonus = 0;
		ac = 0;
	}
	 
	public PlayerCharacter(JSONObject json) {
		initFromJson(json);
	}
	
	private void initFromJson(JSONObject json) {
		try {
			name = (String) json.get("name");
			ac = (int) (long) json.get("ac");
			bonus = (int) (long) json.get("bonus");
		} catch (Exception e) {
			System.out.println("Error in player.PlayerCharacter(JSONObject): " + e.getMessage());
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getAC() {
		return ac;
	}
	
	public int getBonus() {
		return bonus;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setAC(int ac) {
		this.ac = ac;
	}
	
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("name", name);
			obj.put("ac", ac);
			obj.put("bonus", bonus);
		} catch (Exception e) {
			System.out.println("Exception in player.PlayerCharacter.toJson: " + e.getMessage());
		}
		
		return obj;
	}
	
	/*public boolean saveJson() {
		boolean result = false;
		
		try {
			File file = new File("PCs/" + name + ".json");
			
			if (!file.exists())
				file.createNewFile();
				
			FileWriter out = new FileWriter(file);
			out.write(toJsonString());
			out.flush();
			out.close();
			result = true;
			
			System.out.println("monster.Monster " + name + " save successful.");
		} catch (Exception e) {
			System.out.println("Error in monster.Monster.saveJson: " + e.getMessage());
		}
		
		return true;
	 }*/
 }
		 

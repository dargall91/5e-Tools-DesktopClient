package monster;

import org.json.simple.JSONObject;

public class Skill {
	boolean proficient, expertise;
	private String skill;
	
	public Skill() {
		proficient = false;
		expertise = false;
	}
	
	/**
	 * Constructs a monster ability score from json styled string
	 */
	/*Skill(String jsonString) {
		this(new JSONObject(jsonString));
	}*/
	
	/**
	 * Constructs a monster ability from json object
	 */
	Skill(JSONObject jsonObj) {
		try {
			skill = (String) jsonObj.get("skill");
			proficient = (boolean) jsonObj.get("proficient");
			expertise = (boolean) jsonObj.get("expertise");
		} catch (Exception e) {
			System.out.println("Error in monster.Action(JsonObject): " + e.getMessage());
		}
	}
	
	public boolean getProficient() {
		return proficient;
	}
	
	public boolean getExpertise() {
		return expertise;
	}
	
	public void setSkill(String skill) {
		this.skill = skill;
	}
	
	public void setProficient(boolean proficient) {
		this.proficient = proficient;
	}
	
	public void setExpertise(boolean expertise) {
		this.expertise = expertise;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("skill", skill);
			obj.put("proficient", proficient);
			obj.put("expertise", expertise);
		} catch (Exception e) {
			System.out.println("Error in monster.Ability.toJson: " + e.getMessage());
		}
		
		return obj;
	}
}

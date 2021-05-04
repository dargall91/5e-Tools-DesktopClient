package monster;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//TODO: remove proficiency var, is now calculated based on CR. Update desktop GUIs to reflect this change
//TODO: update GUIs with display name
//TODO: update GUIs, leg action coutn now an int
public class Monster {
	private final String[] STATS = { "STR", "DEX", "CON", "INT", "WIS", "CHA" };
	private String name, displayName, type, alignment, size, speed, languages, senses, ac, hp,
		challenge;
	int legendaryActionCount;
	Hashtable<String, AbilityScore> scores;
	Hashtable<String, Skill> skills;
	ArrayList<Ability> abilities;
	ArrayList<Action> actions;
	ArrayList<LegendaryAction> legendaryActions;

	/**
	 * Default constructor, creates an empty monster object
	 */
	public Monster() {
		scores = new Hashtable<String, AbilityScore>();
		skills = new Hashtable<String, Skill>();
		abilities = new ArrayList<Ability>();
		actions = new ArrayList<Action>();
		legendaryActions = new ArrayList<LegendaryAction>();

		FileReader reader = null;
		
		try {
			//InputStream in = this.getClass().getClassLoader().getResourceAsStream("EmptyData/NewMonster.json");
			reader = new FileReader("EmptyData/NewMonster.json", Charset.forName("UTF-8"));

			//if (in == null)
            //		in = new FileInputStream(new File("EmptyData/NewMonster.json"));

			JSONParser parser = new JSONParser();

			//JSONObject json = new JSONObject(new JSONTokener(in));
			JSONObject json = (JSONObject) parser.parse(reader);
			initFromJson(json);
		} catch (Exception e) {
			System.out.println("Error in monster.Monster(String name): " + e.getMessage());
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
	
	/**
	 * Constructs a monster from a json file
	 */
	public Monster(String name) {
		FileReader reader = null;

		try {
			//InputStream in = this.getClass().getClassLoader().getResourceAsStream("Monsters/" + name + ".json");

			reader = new FileReader("Monsters/" + name + ".json", Charset.forName("UTF-8"));

			//if (in == null)
			//		in = new FileInputStream(new File("Encounters/" + name + ">json"));

			JSONParser parser = new JSONParser();

			//JSONObject json = new JSONObject(new JSONTokener(in));
			JSONObject json = (JSONObject) parser.parse(reader);
			initFromJson(json);
		} catch (Exception e) {
			System.out.println("Error in monster.Monster(String name): " + e.getMessage());
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
	
	/**
	 * Constructs a monster.Monster from a JSONObject
	 */
	public Monster(JSONObject json) {
		initFromJson(json);
	}
	
	private void initFromJson(JSONObject json) {
		scores = new Hashtable<String, AbilityScore>();
		skills = new Hashtable<String, Skill>();
		abilities = new ArrayList<Ability>();
		actions = new ArrayList<Action>();
		legendaryActions = new ArrayList<LegendaryAction>();
		
		try {
			name = (String) json.get("name");
			displayName = (String) json.get("displayName");

			if (displayName.equals("")) {
				displayName = name;
			}

			type = (String) json.get("type");
			alignment = (String) json.get("alignment");
			size = (String) json.get("size");
			ac = (String) json.get("ac");
			hp = (String) json.get("hp");
			challenge = (String) json.get("challenge");
			speed = (String) json.get("speed");
			senses = (String) json.get("senses");
			languages = (String) json.get("languages");
			
			JSONArray arr = (JSONArray) json.get("scores");
			int length = arr.size();
			
			for (int i = 0; i < length; i++) {
				JSONObject scoreObj = (JSONObject) arr.get(i);//monster.AbilityScore score = new monster.AbilityScore(arr.getJSONObject(STATS[i]));
				scores.put((String) scoreObj.get("stat"), new AbilityScore(scoreObj));
			}
			
			arr = (JSONArray) json.get("skills");
			length = arr.size();
			
			for (int i = 0; i < length; i++) {
				JSONObject skillObj = (JSONObject) arr.get(i);
				skills.put((String) skillObj.get("skill"), new Skill(skillObj));
			}
			
			arr = (JSONArray) json.get("abilities");
			length = arr.size();
			
			for (int i = 0; i < length; i++) {
				Ability ability = new Ability((JSONObject) arr.get(i));
				abilities.add(ability);
			}
			
			arr = (JSONArray) json.get("actions");
			length = arr.size();
			
			for (int i = 0; i < length; i++) {
				Action action = new Action((JSONObject) arr.get(i));
				actions.add(action);
			}
			
			legendaryActionCount = (int) (long) json.get("legendaryActionCount");
			
			arr = (JSONArray) json.get("legendaryActions");
			length = arr.size();
			
			for (int i = 0; i < length; i++) {
				LegendaryAction legendaryAction = new LegendaryAction((JSONObject) arr.get(i));
				legendaryActions.add(legendaryAction);
			}
		} catch (Exception e) {
			System.out.println("Error in monster.Monster(JSONObject): " + e.getMessage());
		}
	}
	
	//getters
	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getType() {
		return type;
	}
	
	public String getAlignment() {
		return alignment;
	}
	
	public String getSize() {
		return size;
	}
	
	public String getSpeed() {
		return speed;
	}

	public String getLanguages() {
		return languages;
	}
	
	public boolean getSkillProficienct(String skill) {
		return skills.get(skill).getProficient();
	}
	
	public boolean getSkillExpertise(String skill) {
		return skills.get(skill).getExpertise();
	}
	
	public String getAC() {
		return ac;
	}
	
	public String getHP() {
		return hp;
	}
	
	public int getProficiency() {
		if (Objects.isNull(challenge))
			return 0;

		switch (challenge) {
			case "-1":
			case "0":
			case "1/8":
			case "1/4":
			case "1/2":
			case "1":
			case "2":
			case "3":
			case "4":
				return 2;
			case "5":
			case "6":
			case "7":
			case "8":
				return 3;
			case "9":
			case "10":
			case "11":
			case "12":
				return 4;
			case "13":
			case "14":
			case "15":
			case "16":
				return 5;
			case "17":
			case "18":
			case "19":
			case "20":
				return 6;
			case "21":
			case "22":
			case "23":
			case "24":
				return 7;
			case "25":
			case "26":
			case "27":
			case "28":
				return 8;
			case "29":
			case "30":
				return 9;
			default:
				return 0;
		}
	}
	
	public String getChallenge() {
		return challenge;
	}
	
	public int getXP() {
		if (Objects.isNull(challenge))
			return 0;

		switch (challenge) {
			case "-1":
				return 0;
			case "0":
				return 10;
			case "1/8":
				return 25;
			case "1/4":
				return 50;
			case "1/2":
				return 100;
			case "1":
				return 200;
			case "2":
				return 450;
			case "3":
				return 700;
			case "4":
				return 1100;
			case "5":
				return 1800;
			case "6":
				return 2300;
			case "7":
				return 2900;
			case "8":
				return 3900;
			case "9":
				return 5000;
			case "10":
				return 5900;
			case "11":
				return 7200;
			case "12":
				return 8400;
			case "13":
				return 10000;
			case "14":
				return 11500;
			case "15":
				return 13000;
			case "16":
				return 15000;
			case "17":
				return 18000;
			case "18":
				return 20000;
			case "19":
				return 22000;
			case "20":
				return 25000;
			case "21":
				return 33000;
			case "22":
				return 41000;
			case "23":
				return 50000;
			case "24":
				return 62000;
			case "25":
				return 75000;
			case "26":
				return 90000;
			case "27":
				return 105000;
			case "28":
				return 120000;
			case "29":
				return 135000;
			case "30":
				return 155000;
			default:
				return 666;
		}
	}
	
	public String getSenses() {
		return senses;
	}
	
	public int getLegendaryActionCount() {
		return legendaryActionCount;
	}
	
	public int getAbilityScore(String stat) {
		return scores.get(stat).getScore();
	}

	public int getAbilityModifier(String stat) {
		return (scores.get(stat).getScore() - 10) / 2;
	}

	public String getSignedAbilityModifier(String stat) {
		int mod = getAbilityModifier(stat);

		if (mod < 0)
			return Integer.toString(mod);

		return "+" + Integer.toString(mod);
	}
	
	public boolean getAbilityProficiency(String stat) {
		return scores.get(stat).getProficient();
	}
	
	public ArrayList<Action> getActions() {
		return actions;
	}
	
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}
	
	public ArrayList<LegendaryAction> getLegendaryActions() {
		return legendaryActions;
	}
	
	public int getInitiativeBonus() {
		return Math.floorDiv(getAbilityScore("DEX") - 10, 2);
	}
	
	//Setters
	public void setName(String name) {
		this.name = name;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}
	
	public void setSkillProficiency(String skill, boolean proficient) {
		if (!proficient)
			skills.get(skill).setExpertise(proficient);
		
		skills.get(skill).setProficient(proficient);
	}
	
	public void setSkillExpertise(String skill, boolean expertise) {
		if (expertise)
			skills.get(skill).setProficient(expertise);
			
		skills.get(skill).setExpertise(expertise);			
	}
	
	public void setAC(String ac) {
		this.ac = ac;
	}
	
	public void setHP(String hp) {
		this.hp = hp;
	}
	
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
	
	public void setSenses(String senses) {
		this.senses = senses;
	}
	
	public void setLegendaryActionCount(int count) {
		legendaryActionCount = count;
	}
	
	public void setAbilityScore(String stat, int score) {
		scores.get(stat).setScore(score);
	}
	
	public void setAbilityProficiency(String stat, boolean proficient) {
		scores.get(stat).setProficient(proficient);
	}
	
	public void setAbilityDescription(String description, int index) {
		abilities.get(index).setDescription(description);
	}
	
	public void setActionDescription(String description, int index) {
		actions.get(index).setDescription(description);
	}
	
	public void setLegendaryDescription(String description, int index) {
		legendaryActions.get(index).setDescription(description);
	}

	public void setLegendaryActionCost(int cost, int index) {
		legendaryActions.get(index).setCost(cost);
	}
	
	//add actions/abilities
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void addAbility(Ability ability) {
		abilities.add(ability);
	}
	
	public void addLegendaryAction(LegendaryAction action) {
		legendaryActions.add(action);
	}
	
	//delete actions/abilities
	public void deleteAction(int index) {
		actions.remove(index);
	}
	
	public void deleteAbility(int index) {
		abilities.remove(index);
	}
	
	public void deleteLegendaryAction(int index) {
		legendaryActions.remove(index);
	}
	
	//rename actions/abilities
	public void renameAction(String name, int index) {
		actions.get(index).setName(name);
	}
	
	public void renameAbility(String name, int index) {
		abilities.get(index).setName(name);
	}
	
	public void renameLegendaryAction(String name, int index) {
		legendaryActions.get(index).setName(name);
	}
	
	public String toJsonString() {
		String result = "{}";
		
		try {
			result = toJson().toJSONString();
		} catch (Exception e) {
			System.out.println("Error in monster.Monster.toJsonString: " + e.getMessage());
		}
		
		return result;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		JSONArray scoresArr = new JSONArray();
		JSONArray skillsArr = new JSONArray();
		JSONArray abilityArr = new JSONArray();
		JSONArray actionArr = new JSONArray();
		JSONArray legendaryArr = new JSONArray();
		
		try {
			obj.put("name", name);
			obj.put("displayName", displayName);
			obj.put("type", type);
			obj.put("alignment", alignment);
			obj.put("size", size);
			obj.put("ac", ac);
			obj.put("hp", hp);
			obj.put("challenge", challenge);
			obj.put("speed", speed);
			obj.put("senses", senses);
			obj.put("languages", languages);
			
			for (int i = 0; i < STATS.length; i++) {
				scoresArr.add(scores.get(STATS[i]).toJson());
			}
				
			obj.put("scores", scoresArr);
			
			Set<String> keys = skills.keySet();
			Iterator<String> itr = keys.iterator();
			
			while(itr.hasNext()) {
				skillsArr.add(skills.get(itr.next()).toJson());
			}
				
			obj.put("skills", skillsArr);
			
			for (Ability i : abilities)
				abilityArr.add(i.toJson());

			obj.put("abilities", abilityArr);
			
			for (Action i : actions)
				actionArr.add(i.toJson());

			obj.put("actions", actionArr);
			obj.put("legendaryActionCount", legendaryActionCount);
			
			if (legendaryActions.size() > 0)
				for (LegendaryAction i : legendaryActions)
					legendaryArr.add(i.toJson());
				
			obj.put("legendaryActions", legendaryArr);
		} catch (Exception e) {
			System.out.println("Exception in monster.Monster.toJson: " + e.getMessage());
		}

		return obj;
	}
	
	public boolean saveJson() {
		boolean result = false;
		
		try {
			File file = new File("Monsters/" + name + ".json");
			
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
		
		return result;
	}
}

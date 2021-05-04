package client;

import library.*;
import monster.*;
import encounter.*;
import player.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

public class DNDClientProxy implements DNDLibrary {
	
	private static final int buffSize = 6144;
	private String host;
	private int port;
	
	public DNDClientProxy(String host, int port) {
		this.host = host;
		this.port = port;
	}

	private String callMethod(String library, String method, Object[] params) {
		JSONObject call = new JSONObject();
		String result = "";

		try {
			ArrayList<Object> list = new ArrayList();

			call.put("library", library);
			call.put("method", method);

			for (int i = 0; i <  params.length; i++)
				list.add(params[i]);

			JSONArray jsonParams = new JSONArray(list);
			call.put("params", jsonParams);

			Socket sock = new Socket(host, port);
			OutputStream out = sock.getOutputStream();
			InputStream in = sock.getInputStream();

			String request = call.toString();
			byte[] bytesToSend = request.getBytes();
			System.out.println("Request: " + request);
			System.out.println("length: " + bytesToSend.length);
			//out.write(bytesToSend, 0, bytesToSend.length);
			PrintWriter writer = new PrintWriter(out, true);
			writer.println(request);

			//TODO: update read to use BufferedReader to maintain consitency with server
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[buffSize];

			for (int i; (i = in.read(buffer)) != -1; )
				baos.write(buffer, 0, i);

			result = baos.toString();

			out.close();
			in.close();
			sock.close();
		} catch (Exception e) {
			System.out.println("Exception in callMethod: " + e.getMessage());
			result = "{}";
		}

		return result;
	}
	
	public Monster getMonster(String name) {
		String result = callMethod("src/java/monster", "get", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jObj = new JSONObject(result);
		return new Monster(jObj.getJSONObject("result"));
	}
	
	public Encounter getEncounter(String name) {
		String result = callMethod("encounter", "get", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jObj = new JSONObject(result);
		return new Encounter(jObj.getJSONObject("result"));
	}
	
	public PlayerCharacter getPlayerCharacter(String name) {
		String result = callMethod("pc", "get", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jObj = new JSONObject(result);
		return new PlayerCharacter(jObj.getJSONObject("result"));
	}
	
	public boolean addMonster(String name) {
		String result = callMethod("src/java/monster", "add", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean addEncounter(String name) {
		String result = callMethod("encounter", "add", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean addPlayerCharacter(String name) {
		String result = callMethod("pc", "add", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean deleteMonster(String name) {
		String result = callMethod("src/java/monster", "delete", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean deleteEncounter(String name) {
		String result = callMethod("encounter", "delete", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean deletePlayerCharacter(String name) {
		String result = callMethod("pc", "delete", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean renameMonster(String oldName, Monster monster) {
		String result = callMethod("src/java/monster", "change", new Object[]{oldName, monster.toJson()});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	public boolean renameEncounter(String oldName, Encounter encounter) {
		String result = callMethod("encounter", "change", new Object[]{oldName, encounter.toJson()});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean updateMonster(Monster monster) {
		String result = callMethod("src/java/monster", "update", new Object[]{monster.toJson()});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean updateEncounter(Encounter encounter) {
		String result = callMethod("encounter", "update", new Object[]{encounter.toJson()});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean updatePlayerCharacter(PlayerCharacter pc) {
		String result = callMethod("pc", "update", new Object[]{pc.toJson()});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	 
	public boolean restoreMonster(String name) {
		String result = callMethod("src/java/monster", "restore", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean restoreEncounter(String name) {
		String result = callMethod("encounter", "restore", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public ArrayList<String> getMonsterList() {
		String result = callMethod("src/java/monster", "list", new Object[0]);
		//System.out.println("Received " + result + " from server.");
		JSONObject jObj = new JSONObject(result);
		JSONArray jArr = jObj.getJSONArray("result");
		ArrayList<String> list = new ArrayList();
		
		for (int i = 0; i < jArr.length(); i++)
			list.add(jArr.getString(i));
			
		return list;
	}
	
	public ArrayList<String> getEncounterList() {
		String result = callMethod("encounter", "list", new Object[0]);
		//System.out.println("Received " + result + " from server.");
		JSONObject jObj = new JSONObject(result);
		JSONArray jArr = jObj.getJSONArray("result");
		ArrayList<String> list = new ArrayList();
		
		for (int i = 0; i < jArr.length(); i++)
			list.add(jArr.getString(i));
			
		return list;
	}
	
	public ArrayList<String> getPlayerCharacterList() {
		String result = callMethod("pc", "list", new Object[0]);
		//System.out.println("Received " + result + " from server.");
		JSONObject jObj = new JSONObject(result);
		JSONArray jArr = jObj.getJSONArray("result");
		ArrayList<String> list = new ArrayList();
		
		for (int i = 0; i < jArr.length(); i++)
			list.add(jArr.getString(i));
			
		return list;
	}
	
	public boolean saveMonster(String name) {
		String result = callMethod("src/java/monster", "save", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean saveEncounter(String name) {
		String result = callMethod("encounter", "save", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}

	public ArrayList<String> getMusicList() {
		String result = callMethod("encounter", "music", new Object[0]);
		JSONObject jObj = new JSONObject(result);
		JSONArray jArr = jObj.getJSONArray("result");
		ArrayList<String> list = new ArrayList();

		for (int i = 0; i < jArr.length(); i++)
			list.add(jArr.getString(i));

		return list;
	}
	
	public boolean savePlayerCharacters() {
		String result = callMethod("pc", "save", new Object[0]);
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
	
	public boolean saveAll() {
		String result = callMethod("all", "save", new Object[0]);
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}

	public boolean startEncounter(String name) {
		String result = callMethod("combat", "begin", new Object[]{name});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}

	public boolean updateEncounter(JSONArray array) {
		String result = callMethod("combat", "update", new Object[]{array});
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}

	public boolean endEncounter() {
		String result = callMethod("combat", "end", new Object[0]);
		//System.out.println("Received " + result + " from server.");
		JSONObject jsonResult = new JSONObject(result);
		return jsonResult.optBoolean("result", false);
	}
}

package client;

import library.*;
import monster.*;
import encounter.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import player.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

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

			JSONArray jsonParams = new JSONArray();
			jsonParams.addAll(list);
			call.put("params", jsonParams);

			Socket sock = new Socket(host, port);
			OutputStream out = sock.getOutputStream();
			InputStream in = sock.getInputStream();

			String request = call.toString();
			byte[] bytesToSend = request.getBytes();
			System.out.println();
			System.out.println("Request: " + request);
			//System.out.println("length: " + bytesToSend.length);
			//out.write(bytesToSend, 0, bytesToSend.length);
			PrintWriter writer = new PrintWriter(out, true);
			writer.println(request);

			//TODO: update read to use BufferedReader to maintain consistency with server
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

		//System.out.println("Received " + result + " from server.");

		return result;
	}
	
	public Monster getMonster(String name) {
		String result = callMethod("monster", "get", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Monster();
		}
		
		return new Monster((JSONObject) jsonResult.get("result"));
	}
	
	public Encounter getEncounter(String name) {
		String result = callMethod("encounter", "get", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Encounter();
		}
		return new Encounter((JSONObject) jsonResult.get("result"));
	}
	
	public PlayerCharacter getPlayerCharacter(String name) {
		String result = callMethod("pc", "get", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return new PlayerCharacter();
		}
		return new PlayerCharacter((String) jsonResult.get("result"));
	}
	
	public boolean addMonster(String name) {
		String result = callMethod("monster", "add", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean addEncounter(String name) {
		String result = callMethod("encounter", "add", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean addPlayerCharacter(String name) {
		String result = callMethod("pc", "add", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean deleteMonster(String name) {
		String result = callMethod("monster", "delete", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean deleteEncounter(String name) {
		String result = callMethod("encounter", "delete", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean deletePlayerCharacter(String name) {
		String result = callMethod("pc", "delete", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean renameMonster(String oldName, Monster monster) {
		String result = callMethod("monster", "change", new Object[]{oldName, monster.toJson()});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	public boolean renameEncounter(String oldName, Encounter encounter) {
		String result = callMethod("encounter", "change", new Object[]{oldName, encounter.toJson()});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean updateMonster(Monster monster) {
		String result = callMethod("monster", "update", new Object[]{monster.toJson()});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean updateEncounter(Encounter encounter) {
		String result = callMethod("encounter", "update", new Object[]{encounter.toJson()});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean updatePlayerCharacter(PlayerCharacter pc) {
		String result = callMethod("pc", "update", new Object[]{pc.toJson()});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	 
	public boolean restoreMonster(String name) {
		String result = callMethod("monster", "restore", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean restoreEncounter(String name) {
		String result = callMethod("encounter", "restore", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public ArrayList<String> getMonsterList() {
		String result = callMethod("monster", "list", new Object[0]);
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		JSONArray jArr = (JSONArray) jsonResult.get("result");
		ArrayList<String> list = new ArrayList();
		
		for (int i = 0; i < jArr.size(); i++)
			list.add((String) jArr.get(i));
			
		return list;
	}
	
	public ArrayList<String> getEncounterList() {
		String result = callMethod("encounter", "list", new Object[0]);
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		JSONArray jArr = (JSONArray) jsonResult.get("result");
		ArrayList<String> list = new ArrayList();
		
		for (int i = 0; i < jArr.size(); i++)
			list.add((String) jArr.get(i));
			
		return list;
	}
	
	public ArrayList<String> getPlayerCharacterList() {
		String result = callMethod("pc", "list", new Object[0]);
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		JSONArray jArr = (JSONArray) jsonResult.get("result");
		ArrayList<String> list = new ArrayList();
		
		for (int i = 0; i < jArr.size(); i++)
			list.add((String) jArr.get(i));
			
		return list;
	}
	
	public boolean saveMonster(String name) {
		String result = callMethod("monster", "save", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean saveEncounter(String name) {
		String result = callMethod("encounter", "save", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}

	public ArrayList<String> getMusicList() {
		String result = callMethod("encounter", "music", new Object[0]);
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}
		JSONArray jArr = (JSONArray) jsonResult.get("result");
		ArrayList<String> list = new ArrayList();

		for (int i = 0; i < jArr.size(); i++)
			list.add((String) jArr.get(i));

		return list;
	}
	
	public boolean savePlayerCharacters() {
		String result = callMethod("pc", "save", new Object[0]);
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
	
	public boolean saveAll() {
		String result = callMethod("all", "save", new Object[0]);
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}

	public boolean startEncounter(String name) {
		String result = callMethod("combat", "begin", new Object[]{name});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}

	public boolean updateEncounter(JSONArray array) {
		String result = callMethod("combat", "update", new Object[]{array});
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}

	public boolean endEncounter() {
		String result = callMethod("combat", "end", new Object[0]);
		JSONParser parser = new JSONParser();
		JSONObject jsonResult = null;
		
		try {
			jsonResult = (JSONObject) parser.parse(result);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		return (boolean) jsonResult.get("result");
	}
}

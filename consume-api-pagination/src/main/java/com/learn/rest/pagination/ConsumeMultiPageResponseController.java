package com.learn.rest.pagination;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumeMultiPageResponseController {

	@GetMapping(value = "/page")
	public Integer getTotalGoals(@RequestParam(value = "year", required = true) Integer year,
			@RequestParam(value = "team", required = true) String teamName) {
		String homeUrl = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team1=" + teamName
				+ "&page=" + 1;
		String awayUrl = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team2=" + teamName
				+ "&page=" + 1;
		RestTemplate template = new RestTemplate();
		SampleResponse result = template.getForObject(homeUrl, SampleResponse.class);
		int totalGoals = 0;
		int total_pages = 0;
		total_pages = result.getTotal_pages();
		// home team
		for (int page = 1; page <= total_pages; page++) {
			result = template.getForObject("https://jsonmock.hackerrank.com/api/football_matches?year=" + year
					+ "&team1=" + teamName + "&page=" + page, SampleResponse.class);
			List<MatchData> matchData = result.getData();
			for (MatchData data : matchData) {
				totalGoals += data.getTeam1goals();
			}
		}
		// away team
		result = template.getForObject(awayUrl, SampleResponse.class);
		total_pages = result.getTotal_pages();
		for (int page = 1; page <= total_pages; page++) {
			result = template.getForObject("https://jsonmock.hackerrank.com/api/football_matches?year=" + year
					+ "&team2=" + teamName + "&page=" + page, SampleResponse.class);
			List<MatchData> matchData = result.getData();
			for (MatchData data : matchData) {
				totalGoals += data.getTeam1goals();
			}
		}
		return totalGoals;

	}

	@GetMapping(value = "/page/json")
	public Integer getGoals(@RequestParam(value = "year", required = true) Integer year,
			@RequestParam(value = "team", required = true) String teamName,
			@RequestParam(value = "page", required = true) String pageNum) {
		String homeUrl = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team1=" + teamName
				+ "&page=" + pageNum;
		String awayUrl = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team2=" + teamName
				+ "&page=" + pageNum;
		RestTemplate template = new RestTemplate();
		String response = template.getForObject(homeUrl, String.class);
		JsonParser springParser = JsonParserFactory.getJsonParser();
		Map<String, Object> map = springParser.parseMap(response);
		int total_pages = (int) map.get("total_pages");
		List<Object> list = (List<Object>) map.get("data");
		int goal = 0;
		for (Object o : list) {
			if (o instanceof Map) {
				Map<String, Object> datamap = (Map<String, Object>) o;
				for (Map.Entry<String, Object> entry : datamap.entrySet()) {
					if (entry.getKey() == "team1goals") {
						goal += Integer.valueOf(entry.getValue().toString());
					}
				}
			}

		}
		return goal;
	}

	@GetMapping(value = "/page/parse")
	public Integer getTotalGoal(@RequestParam(value = "year", required = true) Integer year,
			@RequestParam(value = "team", required = true) String teamName) {
		int totalGoals = 0;
		try {
			String homeUrl = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team1="
					+ URLEncoder.encode(teamName, StandardCharsets.UTF_8.toString()) + "&page=";
			String awayUrl = "https://jsonmock.hackerrank.com/api/football_matches?year=" + year + "&team2="
					+ URLEncoder.encode(teamName, StandardCharsets.UTF_8.toString()) + "&page=";
			totalGoals = aggregateTotalGoals(homeUrl) + aggregateTotalGoals(awayUrl);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return totalGoals;
	}
	
	
	/**
	 * Method to invoke REST service based upon url
	 * @param baseUrl
	 * @param year
	 * @param teamName
	 * @return
	 */
	private int aggregateTotalGoals(String baseUrl) {
		int goal = 0;
		int page = 1;
		Object obj;
		JSONObject jsonMap;
		BufferedReader br;
		URL url;
		HttpURLConnection conn;
		String response;
		try {
			url = new URL(baseUrl + page);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			response = br.readLine();
			System.out.println("Output from Server .... \n");
			if (response != null) {
				obj = new JSONParser().parse(response);
				jsonMap = (JSONObject) obj;
				Long totalPages = (Long) jsonMap.get("total_pages");
				while (page <= totalPages.intValue()) {
					System.out.println(response);
					obj = new JSONParser().parse(response);
					jsonMap = (JSONObject) obj;
					List<Object> list = (List<Object>) jsonMap.get("data");
					for (Object o : list) {
						if (o instanceof Map) {
							Map<String, Object> datamap = (Map<String, Object>) o;
							goal += Integer.valueOf(datamap.get("team1goals").toString());
						}
					}
					++page;
					url = new URL(baseUrl + page);
					conn = (HttpURLConnection) url.openConnection();
					br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
					response = br.readLine();
				}
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return goal;
	}
}

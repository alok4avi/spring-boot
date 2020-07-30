package com.learn.rest.pagination;

import java.util.List;
import java.util.Map;

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
		for (int page = 2; page <= total_pages; page++) {
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
		for (int page = 2; page <= total_pages; page++) {
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
}

package com.learn.rest.pagination;

import java.util.List;
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
		for (int page=2; page <= total_pages; page++ ) {
			result = template.getForObject("https://jsonmock.hackerrank.com/api/football_matches?year=" + year
					+ "&team1=" + teamName + "&page=" + page, SampleResponse.class);
			List<MatchData> matchData = result.getData();
			for (MatchData data : matchData) {
				totalGoals += data.getTeam1goals();
			}
		}
		//away team
		result = template.getForObject(awayUrl, SampleResponse.class);
		total_pages = result.getTotal_pages();
		for (int page=2; page <= total_pages; page++ ) {
			result = template.getForObject("https://jsonmock.hackerrank.com/api/football_matches?year=" + year
					+ "&team2=" + teamName + "&page=" + page, SampleResponse.class);
			List<MatchData> matchData = result.getData();
			for (MatchData data : matchData) {
				totalGoals += data.getTeam1goals();
			}
		}
		return totalGoals;

	}

}

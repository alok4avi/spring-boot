package com.learn.rest.pagination;

public class MatchData {
	
	private String competition;
	private int year;
	private String round;
	private String team1;
	private String team2;
	private int team1goals;
	private int team2goals;
	
	public MatchData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCompetition() {
		return competition;
	}
	public void setCompetition(String competition) {
		this.competition = competition;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getRound() {
		return round;
	}
	public void setRound(String round) {
		this.round = round;
	}
	public String getTeam1() {
		return team1;
	}
	public void setTeam1(String team1) {
		this.team1 = team1;
	}
	public String getTeam2() {
		return team2;
	}
	public void setTeam2(String team2) {
		this.team2 = team2;
	}
	public int getTeam1goals() {
		return team1goals;
	}
	public void setTeam1goals(int team1goals) {
		this.team1goals = team1goals;
	}
	public int getTeam2goals() {
		return team2goals;
	}
	public void setTeam2goals(int team2goals) {
		this.team2goals = team2goals;
	}
	
	

}

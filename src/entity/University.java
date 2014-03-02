package entity;

public class University {
	String name;
	String url;
	String location;
	String Region;
	String mapurl;
	String worldrank;
	String intro;
	float Overallscore;
	float Teaching;
	float Internationaloutlook;
	float Industryincome;
	float Research;
	float Citations;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getRegion() {
		return Region;
	}
	public void setRegion(String region) {
		Region = region;
	}
	public String getMapurl() {
		return mapurl;
	}
	public void setMapurl(String mapurl) {
		this.mapurl = mapurl;
	}
	public String getWorldrank() {
		return worldrank;
	}
	public void setWorldrank(String worldrank) {
		this.worldrank = worldrank;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public float getOverallscore() {
		return Overallscore;
	}
	public void setOverallscore(float overallscore) {
		Overallscore = overallscore;
	}
	public float getTeaching() {
		return Teaching;
	}
	public void setTeaching(float teaching) {
		Teaching = teaching;
	}
	public float getInternationaloutlook() {
		return Internationaloutlook;
	}
	public void setInternationaloutlook(float internationaloutlook) {
		Internationaloutlook = internationaloutlook;
	}
	public float getIndustryincome() {
		return Industryincome;
	}
	public void setIndustryincome(float industryincome) {
		Industryincome = industryincome;
	}
	public float getResearch() {
		return Research;
	}
	public void setResearch(float research) {
		Research = research;
	}
	public float getCitations() {
		return Citations;
	}
	public void setCitations(float citations) {
		Citations = citations;
	}
	@Override
	public String toString() {
		return "University [name=" + name + ", url=" + url + ", location="
				+ location + ", Region=" + Region + ", mapurl=" + mapurl
				+ ", worldrank=" + worldrank + ", intro=" + intro
				+ ", Overallscore=" + Overallscore + ", Teaching=" + Teaching
				+ ", Internationaloutlook=" + Internationaloutlook
				+ ", Industryincome=" + Industryincome + ", Research="
				+ Research + ", Citations=" + Citations + "]";
	}
}

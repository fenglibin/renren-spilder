package it.renren.spilder.main;

public class Database{
	String jdbcDriverClass;
	String linkString;
	String username;
	String password;
	public String getJdbcDriverClass() {
		return jdbcDriverClass;
	}
	public void setJdbcDriverClass(String jdbcDriverClass) {
		this.jdbcDriverClass = jdbcDriverClass;
	}
	public String getLinkString() {
		return linkString;
	}
	public void setLinkString(String linkString) {
		this.linkString = linkString;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
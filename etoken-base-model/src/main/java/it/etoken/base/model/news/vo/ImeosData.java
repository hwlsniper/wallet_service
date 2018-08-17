package it.etoken.base.model.news.vo;

public class ImeosData {
	private String _id;
	private String title;
	private String source;
	private String content;
	private long issueTime;
	private boolean publish;
	private int rank;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getIssueTime() {
		return issueTime;
	}
	public void setIssueTime(long issueTime) {
		this.issueTime = issueTime;
	}
	public boolean isPublish() {
		return publish;
	}
	public void setPublish(boolean publish) {
		this.publish = publish;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}

}

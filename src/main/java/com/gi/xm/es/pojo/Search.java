package com.gi.xm.es.pojo;

import java.io.Serializable;


/**
 * 全文检索 pojo
 * @author zhangchunyuan
 *
 */
public class Search  implements Serializable{
	
	private Long id;
	
	/**
	 * 搜索字段
	 */
	private String title;
	
	/**
	 * 摘要详情等
	 */
	private String body;
	/**
	 * 标签
	 */
	private String label;
	/**
	 * 源id 比如项目id  公司id
	 */
	private Long sid;
	/**
	 * 搜索类型 项目,投资机构,公司名等等
	 */
	private Integer type;
	
	/**
	 * 图片关联id
	 */
	private Long sourceId;
	
	/**
	 * 图片字段
	 * @return
	 */
	private String pic;
	
	/**
	 * 点击跳转
	 * @return
	 */
	private String  url;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Long getSid() {
		return sid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}
	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}

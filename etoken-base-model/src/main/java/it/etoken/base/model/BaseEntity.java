package it.etoken.base.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 
 * @author 乐
 * 
 */
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 3432415663661690248L;

	private Long id;
	
	/**
	 * 创建时间
	 */
	private Date createdate;

	/**
	 * 修改时间
	 */
	private Date modifydate;

	/**
	 * 删除标志
	 */
	private Integer deleteflag;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public Integer getDeleteflag() {
		return deleteflag;
	}

	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}

}

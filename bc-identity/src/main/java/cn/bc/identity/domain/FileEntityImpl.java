/**
 * 
 */
package cn.bc.identity.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import cn.bc.core.EntityImpl;

/**
 * 文档实体基类：包含创建人信息和最后修改人信息
 * 
 * @author dragon
 */
@MappedSuperclass
public abstract class FileEntityImpl extends EntityImpl implements FileEntity<Long>{
	private static final long serialVersionUID = 1L;
	private Calendar fileDate;// 文档创建时间
	private Actor author;// 创建人

	// 所属组织的冗余信息，用于提高统计效率用
	private Long authorDepartId;// 所属部门id
	private String authorDepartName;
	private Long authorUnitId;// 所属单位id
	private String authorUnitName;
	
	private Long modifierId;// 最后修改人的id
	private String modifierName;// 最后修改人的名称
	private Calendar modifiedDate;// 最后修改时间

	@Column(name = "MODIFIER_ID")
	public Long getModifierId() {
		return modifierId;
	}

	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}

	@Column(name = "MODIFIER_NAME")
	public String getModifierName() {
		return modifierName;
	}

	@Column(name = "MODIFIED_DATE")
	public Calendar getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	@Column(name = "FILE_DATE")
	public Calendar getFileDate() {
		return fileDate;
	}

	public void setFileDate(Calendar fileDate) {
		this.fileDate = fileDate;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Actor.class)
	@JoinColumn(name = "AUTHOR_ID", referencedColumnName = "ID")
	public Actor getAuthor() {
		return author;
	}

	public void setAuthor(Actor author) {
		this.author = author;
	}

	@Column(name = "AUTHOR_NAME")
	public String getAuthorName() {
		if (this.author != null) {
			return this.author.getName();
		} else {
			return null;
		}
	}

	public void setAuthorName(String authorName) {
		// do nothing
	}

	@Column(name = "AUTHOR_DEPART_ID")
	public Long getAuthorDepartId() {
		return authorDepartId;
	}

	public void setAuthorDepartId(Long departId) {
		this.authorDepartId = departId;
	}

	@Column(name = "AUTHOR_DEPART_NAME")
	public String getAuthorDepartName() {
		return authorDepartName;
	}

	public void setAuthorDepartName(String departName) {
		this.authorDepartName = departName;
	}

	@Column(name = "AUTHOR_UNIT_ID")
	public Long getAuthorUnitId() {
		return authorUnitId;
	}

	public void setAuthorUnitId(Long unitId) {
		this.authorUnitId = unitId;
	}

	@Column(name = "AUTHOR_UNIT_NAME")
	public String getAuthorUnitName() {
		return authorUnitName;
	}

	public void setAuthorUnitName(String unitName) {
		this.authorUnitName = unitName;
	}
}
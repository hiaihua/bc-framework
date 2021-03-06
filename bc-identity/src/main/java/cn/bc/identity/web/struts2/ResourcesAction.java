/**
 * 
 */
package cn.bc.identity.web.struts2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.bc.core.query.condition.Direction;
import cn.bc.core.query.condition.impl.OrderCondition;
import cn.bc.db.jdbc.RowMapper;
import cn.bc.db.jdbc.SqlObject;
import cn.bc.identity.domain.Resource;
import cn.bc.identity.web.ResourceTypeFormater;
import cn.bc.identity.web.SystemContext;
import cn.bc.web.struts2.ViewAction;
import cn.bc.web.ui.html.grid.Column;
import cn.bc.web.ui.html.grid.IdColumn4MapKey;
import cn.bc.web.ui.html.grid.TextColumn4MapKey;
import cn.bc.web.ui.html.page.PageOption;

/**
 * 资源视图Action
 * 
 * @author dragon
 * 
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class ResourcesAction extends ViewAction<Map<String, Object>> {
	private static final long serialVersionUID = 1L;

	@Override
	protected String getFormActionName() {
		return "resource";
	}

	@Override
	public boolean isReadonly() {
		// 组织架构管理或系统管理员
		SystemContext context = (SystemContext) this.getContext();
		return !context.hasAnyRole(getText("key.role.bc.actor"),
				getText("key.role.bc.admin"));
	}

	@Override
	protected OrderCondition getGridDefaultOrderCondition() {
		return new OrderCondition("r.order_", Direction.Asc);
	}

	@Override
	protected SqlObject<Map<String, Object>> getSqlObject() {
		SqlObject<Map<String, Object>> sqlObject = new SqlObject<Map<String, Object>>();

		// 构建查询语句,where和order by不要包含在sql中(要统一放到condition中)
		StringBuffer sql = new StringBuffer();
		sql.append("select p.id as pid,p.name as pname,r.id as id,r.type_ as type,r.order_ as orderNo");
		sql.append(",r.name as name,r.url as url,r.iconclass as iconClass,r.option_ as option_");
		sql.append(" from bc_identity_resource as r");
		sql.append(" left join bc_identity_resource as p on p.id=r.belong");
		sqlObject.setSql(sql.toString());

		// 注入参数
		sqlObject.setArgs(null);

		// 数据映射器
		sqlObject.setRowMapper(new RowMapper<Map<String, Object>>() {
			public Map<String, Object> mapRow(Object[] rs, int rowNum) {
				Map<String, Object> map = new HashMap<String, Object>();
				int i = 0;
				map.put("pid", rs[i++]);
				map.put("pname", rs[i++]);
				map.put("id", rs[i++]);
				map.put("type", rs[i++]);
				map.put("orderNo", rs[i++]);
				map.put("name", rs[i++]);
				map.put("url", rs[i++]);
				map.put("iconClass", rs[i++]);
				map.put("option", rs[i++]);
				return map;
			}
		});
		return sqlObject;
	}

	@Override
	protected List<Column> getGridColumns() {
		List<Column> columns = new ArrayList<Column>();
		columns.add(new IdColumn4MapKey("r.id", "id"));
		columns.add(new TextColumn4MapKey("r.type_", "type",
				getText("resource.type"), 80).setSortable(true)
				.setValueFormater(new ResourceTypeFormater(getModuleTypes())));
		columns.add(new TextColumn4MapKey("p.name", "pname",
				getText("resource.belong"), 90).setSortable(true));
		columns.add(new TextColumn4MapKey("r.order_", "orderNo",
				getText("label.order"), 100).setSortable(true).setDir(
				Direction.Asc));
		columns.add(new TextColumn4MapKey("r.name", "name",
				getText("label.name"), 100).setSortable(true));
		columns.add(new TextColumn4MapKey("r.url", "url",
				getText("resource.url")).setSortable(true));
		columns.add(new TextColumn4MapKey("r.iconclass", "iconClass",
				getText("resource.iconClass"), 100).setSortable(true));
		columns.add(new TextColumn4MapKey("r.option_", "option",
				getText("resource.option"), 100));

		return columns;
	}

	@Override
	protected String getGridRowLabelExpression() {
		return "['name']";
	}

	@Override
	protected String[] getGridSearchFields() {
		return new String[] { "r.order_", "r.name", "r.url", "r.iconclass",
				"r.option_", "p.name" };
	}

	@Override
	protected PageOption getHtmlPageOption() {
		return super.getHtmlPageOption().setWidth(800).setHeight(400)
				.setMinWidth(450).setMinHeight(200);
	}

	/**
	 * 获取资源类型值转换列表
	 * 
	 * @return
	 */
	private Map<String, String> getModuleTypes() {
		Map<String, String> types = new HashMap<String, String>();
		types = new LinkedHashMap<String, String>();
		types.put(String.valueOf(Resource.TYPE_FOLDER),
				getText("resource.type.folder"));
		types.put(String.valueOf(Resource.TYPE_INNER_LINK),
				getText("resource.type.innerLink"));
		types.put(String.valueOf(Resource.TYPE_OUTER_LINK),
				getText("resource.type.outerLink"));
		return types;
	}
}

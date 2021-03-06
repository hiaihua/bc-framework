package cn.bc.subscribe.dao.hibernate.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.bc.core.query.condition.impl.AndCondition;
import cn.bc.core.query.condition.impl.EqualsCondition;
import cn.bc.identity.domain.Actor;
import cn.bc.orm.hibernate.jpa.HibernateCrudJpaDao;
import cn.bc.subscribe.dao.SubscribeActorDao;
import cn.bc.subscribe.domain.Subscribe;
import cn.bc.subscribe.domain.SubscribeActor;

/**
 * 访问者DAO接口的实现
 * 
 * @author lbj
 * 
 */
public class SubscribeActorDaoImpl extends HibernateCrudJpaDao<SubscribeActor> implements SubscribeActorDao {

	private static Log logger = LogFactory.getLog(SubscribeActorDaoImpl.class);
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<SubscribeActor> findList(Subscribe subscribe) {
		EqualsCondition eq=new EqualsCondition("subscribe", subscribe);
		return this.createQuery().condition(eq).list();
	}
	
	public List<Actor> findList2Actor(Subscribe subscribe) {
		List<SubscribeActor> sas=this.findList(subscribe);
		List<Actor> actors=new ArrayList<Actor>();
		for(SubscribeActor sa : sas){
			actors.add(sa.getActor());
		}
		return actors;
	}

	public void delete(Long aid, Long pid) {
		Assert.assertNotNull(aid);
		Assert.assertNotNull(pid);
		
		String sql="delete from bc_subscribe_actor where"
				+" aid="+aid+" and pid="+pid;
		
		if(logger.isDebugEnabled()){
			logger.debug("sql := "+sql);
		}

		this.jdbcTemplate.execute(sql);
	}

	public SubscribeActor find4aidpid(Long aid, Long pid) {
		Assert.assertNotNull(aid);
		Assert.assertNotNull(pid);
		AndCondition ac = new AndCondition();
		ac.add(new EqualsCondition("actor.id", aid));
		ac.add(new EqualsCondition("subscribe.id", pid));
		return this.createQuery().condition(ac).singleResult();
	}

	public void delete(SubscribeActor subscribeActor) {
		this.getJpaTemplate().remove(subscribeActor);
	}

	public void delete(List<SubscribeActor> subscribeActors) {
		if (subscribeActors != null)
			for (SubscribeActor aa : subscribeActors)
				this.getJpaTemplate().remove(aa);
	}

	public void save(Long aid, Long pid, int type) {
		Assert.assertNotNull(aid);
		Assert.assertNotNull(pid);
		
		String sql="insert into bc_subscribe_actor (aid,pid,type_,file_date) values("
				+aid+","+pid+","+type+",now())";
		
		if(logger.isDebugEnabled()){
			logger.debug("sql := "+sql);
		}

		this.jdbcTemplate.execute(sql);
	}

}

package it.renren.spilder.type;

import it.renren.spilder.main.ParentPage;
import it.renren.spilder.util.DBOperator;
import it.renren.spilder.util.log.Log4j;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DedecmsTypesMap implements TypesMap {
	private static Log4j log4j = new Log4j(DedecmsTypesMap.class.getName());
	public Map<Integer,String> getTypesMap(ParentPage parentPageConfig) {
		Map<Integer,String> types = new HashMap<Integer,String>();
		DBOperator dbo = new DBOperator();
		try{			
			if(dbo.getConn()==null){
				dbo.initConn(parentPageConfig.getDatabase().getJdbcDriverClass(),parentPageConfig.getDatabase().getLinkString(),parentPageConfig.getDatabase().getUsername(),parentPageConfig.getDatabase().getPassword());
			}
			dbo.setSql("select id,typename from renrenarctype");
			ResultSet rs = dbo.executeQuery();
			while(rs.next()){
				types.put(rs.getInt("id"), rs.getString("typename"));
			}
			rs.close();
		}catch(Exception e){
			log4j.logError(e);
		}finally{
			dbo.closeConn();
		}
		// TODO Auto-generated method stub
		return types;
	}

}

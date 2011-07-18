package it.renren.spilder.dao.ibatis;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import it.renren.spilder.dao.FeedbackDAO;
import it.renren.spilder.dataobject.FeedbackDO;
import it.renren.spilder.util.StringUtil;

public class IbatisFeedbackDAO extends SqlMapClientDaoSupport implements FeedbackDAO {

    private static final String Insert_Feedback = "Insert_Feedback";
    // 条件后缀，用于支持多个不同的表的查询
    private String              tablePrefix;

    @Override
    public void insertFeedback(FeedbackDO feedbackDO) {
        feedbackDO.setTablePrefix(StringUtil.getTablePrefix(tablePrefix));
        getSqlMapClientTemplate().insert(Insert_Feedback, feedbackDO);
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

}

package it.renren.spilder.xiaoshuo.dataobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BooksExample {
    protected String orderByClause;

    protected List oredCriteria;

    public BooksExample() {
        oredCriteria = new ArrayList();
    }

    protected BooksExample(BooksExample example) {
        this.orderByClause = example.orderByClause;
        this.oredCriteria = example.oredCriteria;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public List getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
    }

    public static class Criteria {
        protected List criteriaWithoutValue;

        protected List criteriaWithSingleValue;

        protected List criteriaWithListValue;

        protected List criteriaWithBetweenValue;

        protected Criteria() {
            super();
            criteriaWithoutValue = new ArrayList();
            criteriaWithSingleValue = new ArrayList();
            criteriaWithListValue = new ArrayList();
            criteriaWithBetweenValue = new ArrayList();
        }

        public boolean isValid() {
            return criteriaWithoutValue.size() > 0
                || criteriaWithSingleValue.size() > 0
                || criteriaWithListValue.size() > 0
                || criteriaWithBetweenValue.size() > 0;
        }

        public List getCriteriaWithoutValue() {
            return criteriaWithoutValue;
        }

        public List getCriteriaWithSingleValue() {
            return criteriaWithSingleValue;
        }

        public List getCriteriaWithListValue() {
            return criteriaWithListValue;
        }

        public List getCriteriaWithBetweenValue() {
            return criteriaWithBetweenValue;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteriaWithoutValue.add(condition);
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("value", value);
            criteriaWithSingleValue.add(map);
        }

        protected void addCriterion(String condition, List values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("values", values);
            criteriaWithListValue.add(map);
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            List list = new ArrayList();
            list.add(value1);
            list.add(value2);
            Map map = new HashMap();
            map.put("condition", condition);
            map.put("values", list);
            criteriaWithBetweenValue.add(map);
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return this;
        }

        public Criteria andIdIn(List values) {
            addCriterion("id in", values, "id");
            return this;
        }

        public Criteria andIdNotIn(List values) {
            addCriterion("id not in", values, "id");
            return this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return this;
        }

        public Criteria andTypeIdIsNull() {
            addCriterion("type_id is null");
            return this;
        }

        public Criteria andTypeIdIsNotNull() {
            addCriterion("type_id is not null");
            return this;
        }

        public Criteria andTypeIdEqualTo(Integer value) {
            addCriterion("type_id =", value, "typeId");
            return this;
        }

        public Criteria andTypeIdNotEqualTo(Integer value) {
            addCriterion("type_id <>", value, "typeId");
            return this;
        }

        public Criteria andTypeIdGreaterThan(Integer value) {
            addCriterion("type_id >", value, "typeId");
            return this;
        }

        public Criteria andTypeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("type_id >=", value, "typeId");
            return this;
        }

        public Criteria andTypeIdLessThan(Integer value) {
            addCriterion("type_id <", value, "typeId");
            return this;
        }

        public Criteria andTypeIdLessThanOrEqualTo(Integer value) {
            addCriterion("type_id <=", value, "typeId");
            return this;
        }

        public Criteria andTypeIdIn(List values) {
            addCriterion("type_id in", values, "typeId");
            return this;
        }

        public Criteria andTypeIdNotIn(List values) {
            addCriterion("type_id not in", values, "typeId");
            return this;
        }

        public Criteria andTypeIdBetween(Integer value1, Integer value2) {
            addCriterion("type_id between", value1, value2, "typeId");
            return this;
        }

        public Criteria andTypeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("type_id not between", value1, value2, "typeId");
            return this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return this;
        }

        public Criteria andNameIn(List values) {
            addCriterion("name in", values, "name");
            return this;
        }

        public Criteria andNameNotIn(List values) {
            addCriterion("name not in", values, "name");
            return this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return this;
        }

        public Criteria andRecommendIsNull() {
            addCriterion("recommend is null");
            return this;
        }

        public Criteria andRecommendIsNotNull() {
            addCriterion("recommend is not null");
            return this;
        }

        public Criteria andRecommendEqualTo(Boolean value) {
            addCriterion("recommend =", value, "recommend");
            return this;
        }

        public Criteria andRecommendNotEqualTo(Boolean value) {
            addCriterion("recommend <>", value, "recommend");
            return this;
        }

        public Criteria andRecommendGreaterThan(Boolean value) {
            addCriterion("recommend >", value, "recommend");
            return this;
        }

        public Criteria andRecommendGreaterThanOrEqualTo(Boolean value) {
            addCriterion("recommend >=", value, "recommend");
            return this;
        }

        public Criteria andRecommendLessThan(Boolean value) {
            addCriterion("recommend <", value, "recommend");
            return this;
        }

        public Criteria andRecommendLessThanOrEqualTo(Boolean value) {
            addCriterion("recommend <=", value, "recommend");
            return this;
        }

        public Criteria andRecommendIn(List values) {
            addCriterion("recommend in", values, "recommend");
            return this;
        }

        public Criteria andRecommendNotIn(List values) {
            addCriterion("recommend not in", values, "recommend");
            return this;
        }

        public Criteria andRecommendBetween(Boolean value1, Boolean value2) {
            addCriterion("recommend between", value1, value2, "recommend");
            return this;
        }

        public Criteria andRecommendNotBetween(Boolean value1, Boolean value2) {
            addCriterion("recommend not between", value1, value2, "recommend");
            return this;
        }

        public Criteria andSpecialrecommendIsNull() {
            addCriterion("specialRecommend is null");
            return this;
        }

        public Criteria andSpecialrecommendIsNotNull() {
            addCriterion("specialRecommend is not null");
            return this;
        }

        public Criteria andSpecialrecommendEqualTo(Boolean value) {
            addCriterion("specialRecommend =", value, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendNotEqualTo(Boolean value) {
            addCriterion("specialRecommend <>", value, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendGreaterThan(Boolean value) {
            addCriterion("specialRecommend >", value, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendGreaterThanOrEqualTo(Boolean value) {
            addCriterion("specialRecommend >=", value, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendLessThan(Boolean value) {
            addCriterion("specialRecommend <", value, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendLessThanOrEqualTo(Boolean value) {
            addCriterion("specialRecommend <=", value, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendIn(List values) {
            addCriterion("specialRecommend in", values, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendNotIn(List values) {
            addCriterion("specialRecommend not in", values, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendBetween(Boolean value1, Boolean value2) {
            addCriterion("specialRecommend between", value1, value2, "specialrecommend");
            return this;
        }

        public Criteria andSpecialrecommendNotBetween(Boolean value1, Boolean value2) {
            addCriterion("specialRecommend not between", value1, value2, "specialrecommend");
            return this;
        }

        public Criteria andAuthorIsNull() {
            addCriterion("author is null");
            return this;
        }

        public Criteria andAuthorIsNotNull() {
            addCriterion("author is not null");
            return this;
        }

        public Criteria andAuthorEqualTo(String value) {
            addCriterion("author =", value, "author");
            return this;
        }

        public Criteria andAuthorNotEqualTo(String value) {
            addCriterion("author <>", value, "author");
            return this;
        }

        public Criteria andAuthorGreaterThan(String value) {
            addCriterion("author >", value, "author");
            return this;
        }

        public Criteria andAuthorGreaterThanOrEqualTo(String value) {
            addCriterion("author >=", value, "author");
            return this;
        }

        public Criteria andAuthorLessThan(String value) {
            addCriterion("author <", value, "author");
            return this;
        }

        public Criteria andAuthorLessThanOrEqualTo(String value) {
            addCriterion("author <=", value, "author");
            return this;
        }

        public Criteria andAuthorLike(String value) {
            addCriterion("author like", value, "author");
            return this;
        }

        public Criteria andAuthorNotLike(String value) {
            addCriterion("author not like", value, "author");
            return this;
        }

        public Criteria andAuthorIn(List values) {
            addCriterion("author in", values, "author");
            return this;
        }

        public Criteria andAuthorNotIn(List values) {
            addCriterion("author not in", values, "author");
            return this;
        }

        public Criteria andAuthorBetween(String value1, String value2) {
            addCriterion("author between", value1, value2, "author");
            return this;
        }

        public Criteria andAuthorNotBetween(String value1, String value2) {
            addCriterion("author not between", value1, value2, "author");
            return this;
        }

        public Criteria andImgIsNull() {
            addCriterion("img is null");
            return this;
        }

        public Criteria andImgIsNotNull() {
            addCriterion("img is not null");
            return this;
        }

        public Criteria andImgEqualTo(String value) {
            addCriterion("img =", value, "img");
            return this;
        }

        public Criteria andImgNotEqualTo(String value) {
            addCriterion("img <>", value, "img");
            return this;
        }

        public Criteria andImgGreaterThan(String value) {
            addCriterion("img >", value, "img");
            return this;
        }

        public Criteria andImgGreaterThanOrEqualTo(String value) {
            addCriterion("img >=", value, "img");
            return this;
        }

        public Criteria andImgLessThan(String value) {
            addCriterion("img <", value, "img");
            return this;
        }

        public Criteria andImgLessThanOrEqualTo(String value) {
            addCriterion("img <=", value, "img");
            return this;
        }

        public Criteria andImgLike(String value) {
            addCriterion("img like", value, "img");
            return this;
        }

        public Criteria andImgNotLike(String value) {
            addCriterion("img not like", value, "img");
            return this;
        }

        public Criteria andImgIn(List values) {
            addCriterion("img in", values, "img");
            return this;
        }

        public Criteria andImgNotIn(List values) {
            addCriterion("img not in", values, "img");
            return this;
        }

        public Criteria andImgBetween(String value1, String value2) {
            addCriterion("img between", value1, value2, "img");
            return this;
        }

        public Criteria andImgNotBetween(String value1, String value2) {
            addCriterion("img not between", value1, value2, "img");
            return this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return this;
        }

        public Criteria andDescriptionIn(List values) {
            addCriterion("description in", values, "description");
            return this;
        }

        public Criteria andDescriptionNotIn(List values) {
            addCriterion("description not in", values, "description");
            return this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return this;
        }

        public Criteria andIsfinishedIsNull() {
            addCriterion("isFinished is null");
            return this;
        }

        public Criteria andIsfinishedIsNotNull() {
            addCriterion("isFinished is not null");
            return this;
        }

        public Criteria andIsfinishedEqualTo(Boolean value) {
            addCriterion("isFinished =", value, "isfinished");
            return this;
        }

        public Criteria andIsfinishedNotEqualTo(Boolean value) {
            addCriterion("isFinished <>", value, "isfinished");
            return this;
        }

        public Criteria andIsfinishedGreaterThan(Boolean value) {
            addCriterion("isFinished >", value, "isfinished");
            return this;
        }

        public Criteria andIsfinishedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("isFinished >=", value, "isfinished");
            return this;
        }

        public Criteria andIsfinishedLessThan(Boolean value) {
            addCriterion("isFinished <", value, "isfinished");
            return this;
        }

        public Criteria andIsfinishedLessThanOrEqualTo(Boolean value) {
            addCriterion("isFinished <=", value, "isfinished");
            return this;
        }

        public Criteria andIsfinishedIn(List values) {
            addCriterion("isFinished in", values, "isfinished");
            return this;
        }

        public Criteria andIsfinishedNotIn(List values) {
            addCriterion("isFinished not in", values, "isfinished");
            return this;
        }

        public Criteria andIsfinishedBetween(Boolean value1, Boolean value2) {
            addCriterion("isFinished between", value1, value2, "isfinished");
            return this;
        }

        public Criteria andIsfinishedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("isFinished not between", value1, value2, "isfinished");
            return this;
        }

        public Criteria andSpilderurlIsNull() {
            addCriterion("spilderUrl is null");
            return this;
        }

        public Criteria andSpilderurlIsNotNull() {
            addCriterion("spilderUrl is not null");
            return this;
        }

        public Criteria andSpilderurlEqualTo(String value) {
            addCriterion("spilderUrl =", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlNotEqualTo(String value) {
            addCriterion("spilderUrl <>", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlGreaterThan(String value) {
            addCriterion("spilderUrl >", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlGreaterThanOrEqualTo(String value) {
            addCriterion("spilderUrl >=", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlLessThan(String value) {
            addCriterion("spilderUrl <", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlLessThanOrEqualTo(String value) {
            addCriterion("spilderUrl <=", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlLike(String value) {
            addCriterion("spilderUrl like", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlNotLike(String value) {
            addCriterion("spilderUrl not like", value, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlIn(List values) {
            addCriterion("spilderUrl in", values, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlNotIn(List values) {
            addCriterion("spilderUrl not in", values, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlBetween(String value1, String value2) {
            addCriterion("spilderUrl between", value1, value2, "spilderurl");
            return this;
        }

        public Criteria andSpilderurlNotBetween(String value1, String value2) {
            addCriterion("spilderUrl not between", value1, value2, "spilderurl");
            return this;
        }
    }
}
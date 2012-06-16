package it.renren.spilder.test;

import it.renren.spilder.dao.ArctinyDAO;
import it.renren.spilder.dataobject.ArctinyDO;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        final ConfigurableApplicationContext ctx = new FileSystemXmlApplicationContext(new String[] { "beans.xml" });
        System.out.println(ctx);
        ArctinyDAO arctinyDAO = (ArctinyDAO)ctx.getBean("arctinyDAO");
        ArctinyDO arctinyDO = new ArctinyDO();
        arctinyDO.setTypeid(121);
        arctinyDO = arctinyDAO.selectArctinyByTypeId(arctinyDO);
        //将插入的自增ID给查询出来
    }

}

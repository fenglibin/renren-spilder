package net.lifegirl.core.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.*;

import com.alibaba.fastjson.JSONObject;

import net.lifegirl.dataobject.bean.ImageType;
import net.lifegirl.dataobject.dao.appengine.AEImageTypeDAO;

public class HttpProcessServlet extends HttpServlet {

    private static final long   serialVersionUID = -5369598714956960397L;
    private static final Logger log              = Logger.getLogger(HttpProcessServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String responseString = "";
        resp.setContentType("text/plain");
        String action = req.getParameter("action");
        if ("saveImageType".equals(action)) {
            AEImageTypeDAO dao = new AEImageTypeDAO();
            ImageType imageType = new ImageType();
            imageType.setCode("qinchun");
            imageType.setName("清纯靓丽");
            dao.insert(imageType);

            imageType = new ImageType();
            imageType.setCode("sex");
            imageType.setName("性感美女");
            dao.insert(imageType);
            responseString = "类型插入成功！";
        } else if ("getImageType".equals(action)) {
            AEImageTypeDAO dao = new AEImageTypeDAO();
            List<ImageType> imageTypeList = dao.query();
            String jsonString = JSONObject.toJSONString(imageTypeList);
            responseString = jsonString;
        }
        resp.getWriter().println(responseString);

    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}

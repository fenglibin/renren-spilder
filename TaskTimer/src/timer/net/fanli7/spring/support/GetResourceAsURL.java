package net.fanli7.spring.support;

import it.renren.timer.util.StringUtil;

import java.io.IOException;

import org.springframework.core.io.Resource;

public class GetResourceAsURL extends GetResource {

    public String getLocation(Resource location, boolean parent) throws IOException {
        String resourceName = location.getURL().toExternalForm();

        if (parent) {
            resourceName = StringUtil.substringBeforeLast(resourceName, "/");
        }

        return resourceName;
    }
}

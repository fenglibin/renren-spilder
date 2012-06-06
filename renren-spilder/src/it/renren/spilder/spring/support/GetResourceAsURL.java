package it.renren.spilder.spring.support;

import org.springframework.core.io.Resource;

import it.renren.spilder.util.StringUtil;

import java.io.IOException;

public class GetResourceAsURL extends GetResource {

    public String getLocation(Resource location, boolean parent) throws IOException {
        String resourceName = location.getURL().toExternalForm();

        if (parent) {
            resourceName = StringUtil.substringBeforeLast(resourceName, "/");
        }

        return resourceName;
    }
}

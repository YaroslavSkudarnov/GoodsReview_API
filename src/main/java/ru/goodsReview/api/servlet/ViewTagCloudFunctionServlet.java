package ru.goodsReview.api.servlet;

import org.json.JSONException;
import ru.goodsReview.api.util.FileUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Artemij Chugreev
 * Date: 04.04.12
 * Time: 11:06
 * email: artemij.chugreev@gmail.com
 * skype: achugr
 */

/**
 * return script for view tag cloud
 * it's experimental method, we need insert thesis in JSON in this script source
 */
@Path("/tagCloudFunction")
public class ViewTagCloudFunctionServlet {

    @GET
    @Path("/script")
    @Produces(MediaType.TEXT_HTML)
    public String jsExample() throws JSONException {
        // Return some cliched textual content
        String page = null;
        try {
            page = FileUtil.readFileAsString("viewTagCloudFunction.js");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return page;
    }

}
package com.coretex.web.controllers;

import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.activeorm.services.SearchService;
import com.coretex.data.SystemInfo;
import com.coretex.data.SystemMemoryInfo;
import com.coretex.server.PluginAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Gerasimenko (g-art) Artem "gerasimenko.art@gmail.com"
 *         create by 09-10-2016
 */
@Controller
@RequestMapping("/")
public class ServerPageController extends AbstractController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PluginAccessor pluginAccessor;

    public static final String PAGE_ROOT = "/server/contexts";

    @RequestMapping(method = RequestMethod.GET)
    public String account(final Model model) {

        model.addAttribute("systemInfo", new SystemInfo());
        model.addAttribute("allWebContexts", pluginAccessor.allWebContexts());
        return PAGE_ROOT;
    }

	@RequestMapping(path = "memUseInfo", method = RequestMethod.GET)
	@ResponseBody
	public SystemMemoryInfo getMemUseInfo() {
		return new SystemInfo().getSystemMemoryInfo();
	}

    @RequestMapping(path = "/reload", method = RequestMethod.GET)
    public String reload(@RequestParam(name = "context") final String contextPath, final Model model) {
        pluginAccessor.reloadWebContext(contextPath);
        return "redirect:/";
    }



}

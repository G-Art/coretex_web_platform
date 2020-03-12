package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.data.DataTableResults;
import com.coretex.commerce.data.minimal.MinimalCategoryData;
import com.coretex.commerce.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.facades.ManufacturerFacade;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.items.cx_core.ProductItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractContentController<MinimalProductData> {

	@Resource
	private ProductService productService;

	@Resource
	private ManufacturerFacade manufacturerFacade;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getProducts(Model model) {
		return "product/products";
	}

	@RequestMapping(path = {"/{uuid}", "/{uuid}/variant/{variantUuid}"}, method = RequestMethod.GET)
	public String getProduct(@PathVariable("uuid") UUID uuid, @PathVariable(value = "variantUuid", required = false) UUID variantUuid, Model model) {
		ProductItem product;

		if (Objects.nonNull(variantUuid)) {
			product = productService.getByUUID(variantUuid);
		} else {
			product = productService.getByUUID(uuid);
		}
		model.addAttribute("product", new ProductForm(product));
		model.addAttribute("images", product.getImages());
		return "product/product";
	}

	@RequestMapping(path = "/new", method = RequestMethod.GET)
	public String getProduct(Model model, @RequestParam(value = "code", required = false) String code, RedirectAttributes redirectAttributes) {
		if (productService.getByCode(code) != null) {
			redirectAttributes.addAttribute("message", String.format("Product with code [%s] already exist", code));
			return redirect("/product/products");
		}
		var productForm = new ProductForm();
		productForm.setCode(code);
		model.addAttribute("product", productForm);
		return "product/product";
	}

	@RequestMapping(path = "/save", method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("productForm") ProductForm form, RedirectAttributes redirectAttributes) {

		var product = getProductFacade().save(form);

		redirectAttributes.addAttribute("message", "Product saved");

		return redirect("/product/" + product.getUuid().toString());
	}

	@RequestMapping(path = "/variant/{uuid}/paginated", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public DataTableResults<MinimalProductData> getPageableVariantList(@PathVariable("uuid") UUID uuid, @RequestParam("draw") String draw, @RequestParam("start") Long start, @RequestParam("length") Long length) {
		return getProductFacade().getVariantsForProduct(uuid, draw, start / length, length);
	}

	@ModelAttribute("categories")
	public List<MinimalCategoryData> getCategories() {
		return getCategoryFacade().categories();
	}

	@ModelAttribute("manufacturers")
	public List<MinimalManufacturerData> getManufacturers() {
		return manufacturerFacade.getAll();
	}

	@Override
	protected PageableDataTableFacade<ProductItem, MinimalProductData> getPageableFacade() {
		return getProductFacade();
	}


}

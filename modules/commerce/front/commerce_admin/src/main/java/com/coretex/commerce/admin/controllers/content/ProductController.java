package com.coretex.commerce.admin.controllers.content;

import com.coretex.commerce.core.services.ProductService;
import com.coretex.commerce.data.DataTableResults;
import com.coretex.commerce.data.forms.ProductForm;
import com.coretex.commerce.data.minimal.MinimalCategoryData;
import com.coretex.commerce.data.minimal.MinimalManufacturerData;
import com.coretex.commerce.data.minimal.MinimalProductData;
import com.coretex.commerce.facades.ManufacturerFacade;
import com.coretex.commerce.facades.PageableDataTableFacade;
import com.coretex.core.general.utils.ItemUtils;
import com.coretex.core.services.bootstrap.impl.CortexContext;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.items.cx_core.ProductItem;
import com.coretex.items.cx_core.VariantProductItem;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractContentController<MinimalProductData> {

	@Resource
	private ProductService productService;

	@Resource
	private ManufacturerFacade manufacturerFacade;

	@Resource
	private CortexContext cortexContext;

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String getProducts(Model model) {
		return "product/products";
	}

	@RequestMapping(path = {"/{uuid}", "/{uuid}/variant/{variantUuid}"}, method = RequestMethod.GET)
	public String getProduct(@PathVariable("uuid") UUID uuid,
							 @PathVariable(value = "variantUuid", required = false) UUID variantUuid,
							 Model model) {
		ProductItem product;
		if (Objects.nonNull(variantUuid)) {
			product = productService.getByUUID(variantUuid);
			model.addAttribute("baseProduct", productService.getByUUID(uuid));
		} else {
			product = productService.getByUUID(uuid);
			if (product instanceof VariantProductItem) {
				return redirect("/product/" + uuid);
			}
		}

		model.addAttribute("product", new ProductForm(product));
		model.addAttribute("images", product.getImages());
		return "product/product";
	}

	@RequestMapping(path = {"/new", "/{uuid}/variant/new"}, method = RequestMethod.GET)
	public String getProduct(Model model,
							 @PathVariable(value = "uuid", required = false) UUID uuid,
							 @RequestParam(value = "code", required = false) String code,
							 @RequestParam(value = "variantType", required = false) String typeCode,
							 RedirectAttributes redirectAttributes) {
		if (productService.getByCode(code) != null) {
			redirectAttributes.addFlashAttribute("message", String.format("Product with code [%s] already exist", code));
			return redirect("/product/products");
		}

		ProductForm productForm = new ProductForm();
		if (Objects.nonNull(uuid)) {
			model.addAttribute("baseProduct", productService.getByUUID(uuid));
			productForm.setVariantType(typeCode);
		}

		productForm.setCode(code);
		model.addAttribute("product", productForm);
		return "product/product";
	}

	@RequestMapping(path = {"/remove/{uuid}"}, method = {RequestMethod.GET, RequestMethod.DELETE})
	public String removeProduct(@PathVariable(value = "uuid") UUID uuid,
								RedirectAttributes redirectAttributes) {
		productService.delete(productService.getByUUID(uuid));
		redirectAttributes.addFlashAttribute("message", "Product removed");

		return redirect("/product");
	}

	@RequestMapping(path = {"/save", "/{uuid}/variant/save"}, method = RequestMethod.POST)
	public String saveProduct(@ModelAttribute("productForm") ProductForm form,
							  @PathVariable(value = "uuid", required = false) UUID uuid,
							  RedirectAttributes redirectAttributes) {

		var product = getProductFacade().save(form, uuid);

		redirectAttributes.addFlashAttribute("message", "Product saved");

		if (Objects.nonNull(uuid)) {
			return redirect("/product/" + uuid + "/variant/" + product.getUuid().toString());
		} else {
			return redirect("/product/" + product.getUuid().toString());
		}

	}

	@RequestMapping(path = "/variant/{uuid}/paginated", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public DataTableResults<MinimalProductData> getPageableVariantList(@PathVariable("uuid") UUID uuid, @RequestParam("draw") String draw, @RequestParam("start") Long start, @RequestParam("length") Long length) {
		return getProductFacade().getVariantsForProduct(uuid, draw, start / length, length);
	}

	@ModelAttribute("variantTypes")
	public Set<String> getVariantTypes() {
		var allSubtypes = ItemUtils.getAllSubtypes(cortexContext.findMetaType(VariantProductItem.ITEM_TYPE));
		return allSubtypes.stream().map(MetaTypeItem::getTypeCode).collect(Collectors.toSet());
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

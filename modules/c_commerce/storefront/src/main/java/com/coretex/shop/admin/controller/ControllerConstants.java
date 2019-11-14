
package com.coretex.shop.admin.controller;

/**
 * Interface contain constant for Controller.These constant will be used throughout
 * sm-shop to  providing constant values to various Controllers being used in the
 * application.
 * @author Umesh A
 *
 */
public interface ControllerConstants {

	interface Tiles {

		String adminDashboard = "admin-dashboard";

		interface ContentImages {
			String addContentImages = "admin-contentImages-add";
			String contentImages = "admin-content-images";
			String fileBrowser = "admin-content-filebrowser";

		}

		interface ContentFiles {
			String addContentFiles = "admin-content-files-add";
			String contentFiles = "admin-content-files";


		}

		interface Content {
			String contentPages = "admin-content-pages";
			String contentPagesDetails = "admin-content-pages-details";

		}

		interface Customer {
			String optionsList = "admin-customer-options-list";
			String optionDetails = "admin-customer-options-details";
			String optionsValuesList = "admin-customer-options-values-list";
			String optionsValueDetails = "admin-customer-options-values-details";
			String optionsSet = "admin-customer-options-set";

		}

		interface Product {
			String productReviews = "catalogue-product-reviews";
			String productPrices = "admin-products-prices";
			String productPrice = "admin-products-price";
			String relatedItems = "admin-products-related";
			String digitalProduct = "admin-products-digital";
			String productImages = "admin-products-images";
			String productImagesUrl = "admin-products-images-url";
			String productKeywords = "admin-products-keywords";
			String customGroups = "admin-products-groups";
			String customGroupsDetails = "admin-products-groups-details";
			String manufacturerList = "admin-products-manufacturer";
			String manufacturerDetails = "admin-products-manufacturer-details";
		}

		interface User {
			String profile = "admin-user-profile";
			String users = "admin-users";
			String password = "admin-user-password";
		}

		interface Store {
			String stores = "admin-stores";
		}


		interface Shipping {
			String shippingMethod = "shipping-method";
			String shippingMethods = "shipping-methods";
			String shippingOptions = "shipping-options";
			String shippingPackaging = "shipping-packaging";
			String customShippingWeightBased = "admin-shipping-custom";
		}

		interface Payment {
			String paymentMethods = "payment-methods";
			String paymentMethod = "payment-method";
		}

		interface Order {
			String orders = "admin-orders";
			String ordersEdit = "admin-orders-edit";
			String ordersTransactions = "admin-orders-transactions";
		}

		interface Configuration {
			String accounts = "config-accounts";
			String email = "config-email";
			String cache = "admin-cache";
			String system = "config-system";
		}

		interface Tax {
			String taxClasses = "tax-classes";
			String taxClass = "tax-class";
			String taxConfiguration = "tax-configuration";
			String taxRates = "tax-rates";
			String taxRate = "tax-rate";
		}

	}
}

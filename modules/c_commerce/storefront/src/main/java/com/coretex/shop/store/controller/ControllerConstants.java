
package com.coretex.shop.store.controller;

/**
 * Interface contain constant for Controller.These constant will be used throughout
 * sm-shop to  providing constant values to various Controllers being used in the
 * application.
 * @author Umesh A
 *
 */
public interface ControllerConstants {

	String REDIRECT = "redirect:";

	interface Tiles {
		interface ShoppingCart {
			String shoppingCart = "maincart";
		}

		interface Category {
			String category = "category";
		}

		interface Product {
			String product = "product";
		}

		interface Items {
			String items_manufacturer = "items.manufacturer";
		}

		interface Customer {
			String customer = "customer";
			String customerLogon = "customerLogon";
			String review = "review";
			String register = "register";
			String changePassword = "customerPassword";
			String customerOrders = "customerOrders";
			String customerOrder = "customerOrder";
			String Billing = "customerAddress";
			String EditAddress = "editCustomerAddress";
		}

		interface Content {
			String content = "content";
			String contactus = "contactus";
		}

		interface Pages {
			String notFound = "404";
			String timeout = "timeout";
		}

		interface Merchant {
			String contactUs = "contactus";
		}

		interface Checkout {
			String checkout = "checkout";
			String confirmation = "confirmation";
		}

		interface Search {
			String search = "search";
		}

		interface Error {
			String accessDenied = "accessDenied";
			String error = "error";
		}


	}

	interface Views {
		interface Controllers {
			interface Registration {
				String RegistrationPage = "shop/customer/registration.html";
			}
		}
	}
}

package com.sample.ec.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sample.ec.model.Cart;
import com.sample.ec.model.Product;
import com.sample.ec.model.PurchaseHistory;
import com.sample.ec.model.Qty;
import com.sample.ec.model.RegistrationUser;
import com.sample.ec.service.EcService;


/**
 * Controller Class
 */
@Controller
public class EcController {

    @Autowired
    private EcService service;

	/**
	 * Move to the index page
	 *
	 * @return
	 */
    @GetMapping("/index")
    private String init(HttpSession session) {
		session.setAttribute("productMap", service.getProductMap());
    	return "index";
    }


    /**
     * Redirect Index Page from Root
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirectIndex1(){
        return "redirect:/index";
    }


    /**
     * Redirect Index Page from index.html
     *
     * @return
     */
    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public String redirectIndex2(){
        return "redirect:/index";
    }


	/**
	 * Method for Index
	 *
	 * It is used to set an authorized user information from the Security Context Holder to session.
	 * We get a login user name from Principal.
	 *
	 * @param session to set authorized user
	 * @return index
	 */
    @RequestMapping("/index")
    private String index(HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		session.setAttribute("userName", userName);
		session.setAttribute("productMap", service.getProductMap());
    	return "index";
    }


	/**
	 * Move to the Registration Page
	 *
	 * @return modelAndView
	 */
	@GetMapping("/registration")
	private ModelAndView registeration() {
        ModelAndView modelAndView = new ModelAndView();
        RegistrationUser user = new RegistrationUser();
        modelAndView.addObject("RegistrationUser", user);
        modelAndView.setViewName("registration");
        return modelAndView;
	}


	/**
	 * Create New User
	 *
	 * @param user
	 * @param bindingResult
	 * @return
	 */
    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@ModelAttribute("RegistrationUser") @Valid RegistrationUser user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        boolean userExists = service.checkSameUser(user);
        if (userExists == true) {
            bindingResult.rejectValue("customerName", "error.user", "There is already a user registered. Please input a different name.");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        }
        else {
            user = service.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully. Your Login ID: ");
            modelAndView.addObject("yourId", user.getCustomerId());
            modelAndView.addObject("RegistrationUser", new RegistrationUser());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }


    /**
     * Move to the product page
     *
     * @param session
     * @return
     */
    @RequestMapping("product")
    private String displayProduct(HttpSession session, @RequestParam("p") String productId) {
    	Product product = service.getProduct(productId);
    	session.setAttribute("product", product);
    	return "product";
    }


    /**
     * Add the product to the customer's list
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/add-flist")
    public ModelAndView addToFavoriteList(HttpSession session) {
    	ModelAndView modelAndView = new ModelAndView();
    	String productId = ((Product)session.getAttribute("product")).getProductId();

    	if(service.addFavoriteList((String)session.getAttribute("userName"), productId) == false) {
    		modelAndView.addObject("errorMessage", "The data already exists in your list.");
    	}
    	else {
        	modelAndView.addObject("successMessage", "We added the item to your list.");
        }
        modelAndView.setViewName("product");
        return modelAndView;
    }


    /**
     * Add the product to the customer's list
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/see-favorites")
    public String seeFavorite(HttpSession session) {
        Qty qty = new Qty();
        String customerName = (String)session.getAttribute("userName");
        session.setAttribute("Qty", qty);
    	session.setAttribute("favoriteList", service.getFavoriteProductList(customerName));
    	return "favorite";
    }


    /**
     * Delete the product from the customer's list
     *
     * @param product id
     * @param session
     * @return
     */
    @RequestMapping(value = "/delete-item")
    public String deleteItem(@RequestParam("p") String productId, HttpSession session) {
    	String customerName = (String)session.getAttribute("userName");
    	service.deleteFavoriteItem(customerName, productId);
    	session.setAttribute("favoriteList", service.getFavoriteProductList(customerName));
    	return "favorite";
    }


    /**
     * Buy the item
     *
     * @param product id
     * @param session
     * @return
     */
    @RequestMapping(value = "/move-next", params = "purchase-now")
    public String purchaseItem(@ModelAttribute("Qty") Qty qty, @RequestParam("p") String productId, HttpSession session) {
    	return BuyProduct(qty.getQty(), productId, session);
    }


    /**
     * Buy Product
     *
     * @param qty
     * @param productId
     * @param session
     * @return
     */
    private String BuyProduct(int qty, String productId, HttpSession session) {
    	String page = "purchase";
    	List<PurchaseHistory> phList = new ArrayList<PurchaseHistory>();
    	String customerName = (String)session.getAttribute("userName");

    	// Buy Product and Save Data as a Purchase History
    	PurchaseHistory ph = service.buyProduct(customerName, productId, qty);

    	if (ph == null) {
    		// Customer cannot buy the item due to some reason. (ex. The item does not exist.)
    		page = "error";
    	}
    	else {
    		phList.add(ph);

        	// Get Address
        	String address = service.getRegisterUserByCustomerName(customerName).getAddress1();

    		session.setAttribute("purchaseHistoryList", phList);
    		session.setAttribute("address", address);
    		session.setAttribute("sum",  (ph.getPrice() * ph.getQty()));
    	}
    	return page;
    }


    /**
     * Add the product to cart
     *
     * @param product id
     * @param session
     * @return
     */
    @RequestMapping(value = "/move-next", params = "add-cart")
    public String addToCart(@ModelAttribute("Qty") Qty qty, @RequestParam("p") String productId, HttpSession session) {
    	String customerName = (String)session.getAttribute("userName");

    	// Add the product in the cart
    	service.addToCart(customerName, productId, qty.getQty());

    	// Get all products in the cart
    	List<Cart> cList  = service.getCartList(customerName);
    	session.setAttribute("cartList", cList);
    	session.setAttribute("sum", service.getCartSum(cList));
    	return "cart";
    }


    /**
     * See cart
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/see-cart")
    public String seeCart(HttpSession session) {
    	setCartList(session);
    	return "cart";
    }


    /**
     * Set Cart List
     *
     * @param session
     */
    private void setCartList(HttpSession session) {
    	String customerName = (String)session.getAttribute("userName");
    	List<Cart> cList  = service.getCartList(customerName);
    	String address = service.getRegisterUserByCustomerName(customerName).getAddress1();

    	session.setAttribute("cartList", cList);
    	session.setAttribute("address", address);
    	session.setAttribute("sum", service.getCartSum(cList));
    }


    /**
     * Delete the item from the customer's list
     *
     * @param product id
     * @param session
     * @return
     */
    @RequestMapping(value = "/delete-cart-item")
    public String deleteCartItem(@RequestParam("p") String productId, HttpSession session) {
    	service.deleteCartItem((String)session.getAttribute("userName"), productId);
    	setCartList(session);
    	return "cart";
    }


    /**
     * Change the quantity of the product in the cart.
     *
     * @param qty
     * @param product id
     * @param session
     * @return
     */
    @RequestMapping(value = "/c_qty", params = "c_qty")
    public String changeQty(@RequestParam("c_qty") int qty, @RequestParam("p") String productId, HttpSession session) {
    	service.updateQtyInCart((String)session.getAttribute("userName"), productId, qty);
    	setCartList(session);
    	return "cart";
    }


    /**
     * Buy items in the cart.
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "purchase-items")
    public String buyItems(HttpSession session) {
    	// Get all cart products
    	List<Cart> cList  = (List<Cart>)session.getAttribute("cartList");

    	// Buy Items & Clear Cart
    	List<PurchaseHistory> phList = service.buyCartProducts(cList);

    	// Get User Address
    	String address = service.getRegisterUserByCustomerName((String)session.getAttribute("userName")).getAddress1();

    	session.setAttribute("purchaseHistoryList", phList);
    	session.setAttribute("address", address);
		session.setAttribute("sum",  service.getPurchaseHistorySum(phList));
    	return "purchase";
    }


    /**
     * Move to the user information page
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "setting")
    public ModelAndView moveSetting(HttpSession session) {
    	ModelAndView modelAndView = new ModelAndView();
    	String customerName = (String)session.getAttribute("userName");
    	RegistrationUser user = service.getRegisterUserByCustomerName(customerName);
        modelAndView.addObject("RegistrationUser", user);
        modelAndView.setViewName("setting");
    	return modelAndView;
    }


    /**
    * Change an user information
    *
    * @return
    */
   @RequestMapping(value = "re-register")
   public ModelAndView changeUserInfo(@ModelAttribute("RegistrationUser") @Valid RegistrationUser user, BindingResult bindingResult, HttpSession session) {
       ModelAndView modelAndView = new ModelAndView();

       if (bindingResult.hasErrors()) {
           modelAndView.setViewName("setting");
       }
       else {
           user = service.updateUserInfo(user);
           modelAndView.addObject("successMessage", "User has been registered successfully.");
           modelAndView.addObject("RegistrationUser", user);
           modelAndView.setViewName("setting");
       }
       return modelAndView;
    }
}
package com.sample.ec.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sample.ec.model.Cart;
import com.sample.ec.model.EcCustomer;
import com.sample.ec.model.Favorite;
import com.sample.ec.model.Product;
import com.sample.ec.model.PurchaseHistory;
import com.sample.ec.model.RegistrationUser;
import com.sample.ec.model.Role;
import com.sample.ec.repository.CartDao;
import com.sample.ec.repository.CartRepository;
import com.sample.ec.repository.EcCustomerDao;
import com.sample.ec.repository.FavoriteDao;
import com.sample.ec.repository.FavoriteRepository;
import com.sample.ec.repository.ProductDao;
import com.sample.ec.repository.ProductRepository;
import com.sample.ec.repository.PurchaseHistoryRepository;
import com.sample.ec.repository.RegistrationRepository;
import com.sample.ec.repository.RoleRepository;

/**
 * Service Class
 */
@Service
public class EcService  implements UserDetailsService {
	// Repository
    private RegistrationRepository registrationRepository;
    private RoleRepository roleRepository;
    private FavoriteRepository favoriteRepository;
    private ProductRepository productRepository;
    private PurchaseHistoryRepository purchaseHistoryRepository;
    private CartRepository cartRepository;

	@Autowired
	private EcCustomerDao eccDao;

	@Autowired
	private ProductDao pDao;

	@Autowired
	private FavoriteDao fDao;

	@Autowired
	private CartDao cDao;

	// Last ID for new creation
    private long lastId = getNow();

    // Hashing
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public EcService(RegistrationRepository registrationRepository, RoleRepository roleRepository, FavoriteRepository favoriteRepository, ProductRepository productRepository, PurchaseHistoryRepository purchaseHistoryRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.registrationRepository = registrationRepository;
        this.roleRepository = roleRepository;
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


	/**
	 * Get Product Map
	 *
	 * We use this method to display all products on the index page.
	 *
	 * @return All products
	 */
	public Map<String, Product> getProductMap(){
		Map<String, Product> pMap = new TreeMap<String, Product>();
		List<String> idList = pDao.getProductIdList();

		for(String id : idList){
			Product product =  pDao.getProduct(id);
			pMap.put(id, product);
		}

		return pMap;
	}


	/**
	 * Get Product Entity by using product id
	 *
	 * @param id
	 * @return
	 */
	public Product getProduct(String productId) {
		return getProductByProductId(productId);
	}


	/**
	 * Get Product Entity by using product id
	 *
	 * @param id
	 * @return
	 */
	private Product getProductByProductId(String productId) {
		return pDao.getProduct(productId);
	}


	/**
	 * Implement the 'UserDetailService' interface
	 */
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		EcCustomer customer = eccDao.findCustomer(id);

		if (customer == null) {
			throw new UsernameNotFoundException("ID: " + id + "was not found.");
		}

		// Get Grant List
		List<GrantedAuthority> grantList = getGrantList();

		// Create User Details, and then we are going to see if the user can log in or not on the config class.
		UserDetails userDetails = (UserDetails)new User(customer.getCustomerName(), bCryptPasswordEncoder.encode(customer.getPassword()), grantList);

		return userDetails;
	}


	/**
	 * Get Grant List
	 *
	 * In the grant list, there are authorities such Admin and User.
	 * We don't use the granted auhorities this time, so we set "USER" in the grant list.
	 * We need to handle them and a create authority table and user authority table when using them.
	 *
	 * @return Grant List
	 */
	private List<GrantedAuthority> getGrantList() {
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("USER");
		grantList.add(authority);
		return grantList;
	}


	/**
	 * Chack Same User
	 *
	 *  We cannot register the user that has the registered email.
	 *  if a user exisits, it returns true. But if not, it returns false.
	 *
	 * @param user
	 * @return
	 */
	public boolean checkSameUser(RegistrationUser user) {
		boolean flag = false;
		EcCustomer rUser = eccDao.getSameUser(user);

		if(rUser != null) {
			flag = true;
		}

		return flag;
	}


    /**
     * Save New User Data
     *
     * @param user
     * @return
     */
    public RegistrationUser saveUser(RegistrationUser user) {
    	Role userRole = roleRepository.findByRole("ADMIN");
    	user.setCustomerId(getNewCustomerId());
		user.setActive(true);
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		return registrationRepository.save(user);
    }


    /**
     * Get New Customer ID
     *
     * @return Unique Customer ID
     */
    private String getNewCustomerId() {
    	// Get Customer ID List
    	List<String> idList = eccDao.getAllCustomerIDList();

    	// Check Same ID
    	String newId = "";
    	do {
    		newId = createNewCustomerId();
    	} while(checkSameCustomerId(idList, newId));

    	return newId;
    }


    /**
     * Create New Customer ID
     *
     * @return
     */
    private String createNewCustomerId() {
    	UUID uuid = UUID.randomUUID();
    	String tmpId = uuid.toString();
    	String newCustomerId = tmpId.substring(tmpId.length() - 10);
    	return newCustomerId;
    }


    /**
     * Check Same Customer ID
     *
     *  true: The created ID exists in the ID list
     *  false: The created ID does not exist.
     *
     * @param idList
     * @param newId
     * @return
     */
    private boolean checkSameCustomerId(List<String> idList, String newId) {
    	Iterator<String> it = idList.iterator();
    	while(it.hasNext()) {
    		String tmpId = it.next();
    		if(tmpId.equals(newId)) {
    			return true;
    		}
    	}
    	return false;
    }


    /**
     * Get Customer ID by using Displayed Customer Name
     *
     * @param customerName
     * @return
     */
    public String getCustomerIdByCustomerName(String customerName) {
    	return eccDao.getCustomerId(customerName);
    }


    /**
     * Get Registration User by using Displayed Customer Name
     *
     * @param customerId
     * @return
     */
    public RegistrationUser getRegisterUserByCustomerName(String customerName) {
    	return registrationRepository.findByCustomerName(customerName);
    }


    /**
     * Add to Favorite List
     *
     * true : We added the product to customer's favorite list.
     * false : The data already exists in the list.
     *
     * @param customerName
     * @param productId
     */
    public boolean addFavoriteList(String customerName, String productId) {
    	boolean addData = false;
    	String customerId = eccDao.getCustomerId(customerName);
    	Favorite favorite = null;

    	try {
    		favorite = fDao.getFavoriteData(customerId, productId);
    	}
		catch (NoResultException nre) {
			// If the product does not exist in the list, it adds the product to the list.
			favorite =  new Favorite();
        	favorite.setCustomerId(customerId);
        	favorite.setProductId(productId);
        	favorite.setDate(getLocalDateTime());
        	favoriteRepository.save(favorite);
			addData = true;
		}

    	return addData;
    }


    /**
     * Get Current Local Date and Time
     * (Over Java 8)
     *
     * @return current date and time
     */
    private String getLocalDateTime() {
    	LocalDateTime currentDateTime = LocalDateTime.now();
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    	String datetime = currentDateTime.format(formatter);
    	return datetime;
    }


    /**
     * Get Favorite Product List
     *
     * @param customerName
     * @return
     */
    public List<Product> getFavoriteProductList(String customerName) {
    	String customerId = eccDao.getCustomerId(customerName);
    	List<Favorite> fList = null;
    	List<Product> pList = null;

    	try {
    		// Get favorite data list
    		fList = fDao.getFavoriteList(customerId);

    		// Get product data list
    		pList = getFavoriteProductList(fList);
    	}
    	catch (NoResultException nre) {
    		System.out.println("The data does not exist in the favorite list.");
    		pList = new ArrayList<Product>();
    	}

    	return pList;
    }

    /**
     * Get Favorite Product List
     *
     * @param fList
     * @return
     */
    private List<Product> getFavoriteProductList(List<Favorite> fList) {
    	List<Product> pList = new ArrayList<Product>();
    	String productId = null;
    	Favorite favorite = null;
    	Product product = null;
    	Iterator<Favorite> it = fList.iterator();

    	while (it.hasNext()){
    		favorite = (Favorite)it.next();
    		productId = favorite.getProductId();
    		product = getProductByProductId(productId);
    		pList.add(product);
    	}
    	productId = null;
    	favorite = null;
    	product = null;

    	return pList;
    }


    /**
     * Delete Favorite Item
     *
     * @param customerName
     * @param productId
     */
    public void deleteFavoriteItem(String customerName, String productId) {
    	String customerId = eccDao.getCustomerId(customerName);
    	try {
    		Favorite favorite = fDao.getFavoriteData(customerId, productId);
    		favoriteRepository.delete(favorite);
    	}
    	catch (NoResultException nre) {
    		System.out.println("The item does not exist on the list.");
    	}
    }


    /**
     * Buy the Item
     *
     * @param customerName
     * @param productId
     * @param qty
     * @return
     */
    public PurchaseHistory buyProduct(String customerName, String productId, int qty) {
    	PurchaseHistory ph = null;
    	Product product = getUpdateProduct(productId, qty);

		if (product == null) {
			// If we don't have the product, the "sold-out" message is displayed.
			ph = getUpdateHistoryNoStock(customerName, productId);
		}
		else {
			// Stock Process
			product.setStock(product.getStock() - qty);
			productRepository.save(product);

			// Purchase History Process
			ph = getUpdateHistory(customerName, productId, product.getProductName(), qty, product.getPrice());
			purchaseHistoryRepository.save(ph);

			// Delete Favorite List after Purchasing
			deleteFavoriteItem(customerName, productId);
		}
		return ph;
    }


    /**
     * Get Product after Checking the Stock.
     *
     * Return product if the user can buy the product.
     * Return null if the user cannot buy the product due to a shortage of stock.
     *
     * @param productId
     * @param qty
     * @return
     */
    private Product getUpdateProduct(String productId, int qty) {
		Product product = pDao.getProduct(productId);
		int stock = product.getStock() - qty;
		if (stock < 0) {
			product = null;
		}
    	return product;
    }


    /**
     * Get Purchase History
     *
     * @param customerName
     * @param productId
     * @param productName
     * @param qty
     * @param price
     * @return
     */
    private PurchaseHistory getUpdateHistory(String customerName, String productId, String productName, int qty, int price) {
    	PurchaseHistory ph = new PurchaseHistory();
    	ph.setPurchaseHistoryId(String.valueOf(createNewPhId()));
    	ph.setCustomerId(eccDao.getCustomerId(customerName));
    	ph.setCustomerName(customerName);
    	ph.setProductId(productId);
    	ph.setProductName(productName);
    	ph.setQty(qty);
    	ph.setPrice(price);
    	ph.setPurchaseDate(getLocalDateTime());
    	return ph;
    }


    /**
     * Create New Puchase History ID
     *
     * We can create 1000 ID at most per one second.
     *
     * @return ID
     */
    private synchronized long createNewPhId() {
        while (true) {
            long newId = getNow();

            // Request if the ID creation is at the same second. Just add one to millisecond.
            if (newId / 1000 == lastId / 1000) {
                if (lastId %1000 < 999) {
                    return ++lastId;
                }

                //  We block the process if the ID is getting over the maximum size(1000).
                try {
                    Thread.sleep(1);
                }
                catch (InterruptedException e) {

                }
                continue;
            }
            else if (newId < lastId) {
                //  We handle the server time that was changed.
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {

                }
                continue;
            }

            return lastId = newId;
        }
    }


    /**
     * Get Current Day and Time
     *
     * @return date
     */
    private static long getNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        long date = calendar.get(Calendar.YEAR) * 10000000000000l;
        date += (calendar.get(Calendar.MONTH)+1) * 100000000000l;
        date += calendar.get(Calendar.DAY_OF_MONTH) * 1000000000l;
        date += calendar.get(Calendar.HOUR_OF_DAY) * 10000000l;
        date += calendar.get(Calendar.MINUTE) * 100000;
        date += calendar.get(Calendar.SECOND) * 1000;
        return date;
    }


    /**
     * Add to Cart
     *
     * @param customerName
     * @param productId
     * @param qty
     */
    public void addToCart(String customerName, String productId, int qty) {
    	String customerId = eccDao.getCustomerId(customerName);
    	Cart cart = null;
    	try {
    		cart = cDao.getCart(customerId, productId);
    		int newQty = cart.getQty() + qty;
    		int newSum = cart.getPrice() * newQty;
    		cartRepository.updateQtyInCart(newQty, newSum, customerId, productId);
    	}
    	catch (NoResultException nre) {
    		System.out.println("The prouduct does not exist in the cart.");
    		cartRepository.save(createCart(customerName, productId, qty));
    	}
    }


    /**
     * Create Cart
     *
     * @param customerName
     * @param productId
     * @param qty
     * @return
     */
    private Cart createCart(String customerName, String productId, int qty) {
    	Product product = pDao.getProduct(productId);
    	Cart cart = new Cart();
    	cart.setCustomerId(eccDao.getCustomerId(customerName));
    	cart.setCustomerName(customerName);
    	cart.setProductId(productId);
    	cart.setProductName(product.getProductName());
    	cart.setQty(qty);
    	cart.setPrice(product.getPrice());
    	cart.setSum(qty * product.getPrice());
    	cart.setStock(product.getStock());
    	cart.setCDate(getLocalDateTime());
    	return cart;
    }


    /**
     * Get Cart List
     *
     * @param customerName
     * @param productId
     * @return
     */
    public List<Cart> getCartList(String customerName) {
    	List<Cart> cList = new ArrayList<Cart>();
    	String customerId = eccDao.getCustomerId(customerName);
    	cList = cDao.findCartList(customerId);
    	return cList;
    }


    /**
     * Get SUM in the cart
     *
     * @param Cart Items List
     * @return
     */
    public int getCartSum(List<Cart> cList) {
    	int sum = 0;
    	if(cList == null) {

    	}
    	else {
	    	Iterator<Cart> it = cList.iterator();
	    	while(it.hasNext()) {
	    		Cart cart = (Cart)it.next();
	    		sum += cart.getSum();
	    	}
    	}
    	System.out.println("Cart Item SUM: "+sum);
    	return sum;
    }


    /**
     * Delete Cart Item
     *
     * @param customerName
     * @param productId
     */
    public void deleteCartItem(String customerName, String productId) {
    	String customerId = eccDao.getCustomerId(customerName);
    	try {
    		Cart cart = cDao.getCart(customerId, productId);
    		cartRepository.delete(cart);
    	}
    	catch (NoResultException nre) {
    		System.out.println("The item does not exist in the "+customerName+"'s cart.");
    	}
    }


    /**
     * Change the quantity of the product in the cart
     *
     * @param customerName
     * @param productId
     * @param qty
     */
    public void updateQtyInCart(String customerName, String productId, int qty){
    	int newSum = pDao.getProduct(productId).getPrice() * qty;
    	String customerId = eccDao.getCustomerId(customerName);
    	try {
    		cartRepository.updateQtyInCart(qty, newSum, customerId, productId);
    	}
    	catch (Exception e) {
    		System.out.println("We cannot change the quantity of the product in the cart.");
    		e.printStackTrace();
    	}
    }


    /**
     * Buy Products on the Cart List.
     *
     * @param cList
     * @return
     */
    public List<PurchaseHistory> buyCartProducts(List<Cart> cList) {
    	List<PurchaseHistory> phList = null;
    	if(cList != null) {
    		phList = buyCartProductsProcess(cList);
    	}
    	else {
    		System.out.println("There seem NOT to be items in the cart.");
    		phList = new ArrayList<PurchaseHistory>();
    	}
    	return phList;
    }


    /**
     *  Buy Products on the Cart List
     *
     *  ・Minus item stock
     *  ・Save the purchase history
     *  ・Delete the cart items
     *
     * @param cList
     * @return
     */
	private List<PurchaseHistory> buyCartProductsProcess(List<Cart> cList){
		List<PurchaseHistory> phList = new ArrayList<PurchaseHistory>();

		Iterator<Cart> it = cList.iterator();
		while(it.hasNext()) {
			PurchaseHistory ph = null;

			Cart cart = it.next();
			String productId = cart.getProductId();
			int qty = cart.getQty();

			// Check the item in stock
			Product product = getUpdateProduct(productId, qty);

			if (product == null) {
				// The product does not exist in stock.
				System.out.println("The product does not exist in stock.");
				ph = getUpdateHistoryNoStock(cart.getCustomerName(), productId);
			}
			else {
				// Buy Product (That means to minus the quantity of the product from the stock.)
				product.setStock(product.getStock() - qty);
				productRepository.save(product);

				// Purchase History Process
				ph = getUpdateHistory(cart.getCustomerName(), productId, product.getProductName(), qty, product.getPrice());
				purchaseHistoryRepository.save(ph);
			}

			// Add Phrchase History
			phList.add(ph);

			// Delete Cart after Purchasing
			cartRepository.delete(cart);
		}

		return phList;
	}


    /**
     * Get Purchase History when the product does not exist in stock.
     *
     * @param customerName
     * @param productId
     * @param productName
     * @param qty
     * @param price
     * @return
     */
    private PurchaseHistory getUpdateHistoryNoStock(String customerName, String productId) {
    	PurchaseHistory ph = new PurchaseHistory();
    	Product product = pDao.getProduct(productId);
    	ph.setPurchaseHistoryId(String.valueOf(createNewPhId()));
    	ph.setCustomerId(eccDao.getCustomerId(customerName));
    	ph.setCustomerName(customerName);
    	ph.setProductId(productId);
    	ph.setProductName(product.getProductName());
    	ph.setQty(0);
    	ph.setPrice(product.getPrice());
    	ph.setPurchaseDate(getLocalDateTime());
    	return ph;
    }


	/**
	 * Get Purchase History SUM
	 *
	 * @param phList
	 * @return
	 */
    public int getPurchaseHistorySum(List<PurchaseHistory> phList) {
    	int sum = 0;
    	if(phList == null) {

    	}
    	else {
	    	Iterator<PurchaseHistory> it = phList.iterator();
	    	while(it.hasNext()) {
	    		PurchaseHistory ph = (PurchaseHistory)it.next();
	    		sum += (ph.getPrice() * ph.getQty());
	    	}
    	}
    	return sum;
    }


    /**
     * Update User Information
     *
     * @param user
     * @return
     */
    public RegistrationUser updateUserInfo(RegistrationUser user) {
    	registrationRepository.updateUser(user.getCustomerName(), user.getPassword(), user.getPhoneNumber(), user.getEmail(), user.getRealName(), user.getAddress1(), user.getCardNum(), user.getCustomerId());
    	return registrationRepository.findByCustomerName(user.getCustomerName());
    }


}

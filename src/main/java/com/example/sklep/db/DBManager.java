package com.example.sklep.db;
public class DBManager {
    private static DBManager instance = null; // наша точка доступа к бд
    //создание единственного обьекта (запрещает использование вне класса)
    private AccountRepository accountRepository;
    private CartItemRepository cartItemRepository;
    private CategoryRepository categoryRepository;
    private ItemRepository itemRepository;
    private OrderRepository orderRepository;
    private ProfileRepository profileRepository;
    private DBManager(){

        accountRepository = new AccountRepository(null);
        categoryRepository =  new CategoryRepository(null);
        cartItemRepository =  new CartItemRepository(null);
        itemRepository = new ItemRepository(null);
        orderRepository = new OrderRepository(null);
        profileRepository = new ProfileRepository(null);


    }
    public static DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();//если еще не создан
        return instance;
    }
}

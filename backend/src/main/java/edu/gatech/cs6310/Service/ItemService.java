package edu.gatech.cs6310.Service;

import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.Item;
import edu.gatech.cs6310.Repo.ItemRepo;
import edu.gatech.cs6310.utility.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service("itemService")
public class ItemService {

    @Autowired
    @Qualifier("itemRepo")
    private ItemRepo itemRepo;

    public boolean save(Item item) {
        if (itemRepo.findByStoreAndName(item.getStore(), item.getName()) != null)
        {
            Messages.displayErrorMessage("item_identifier_already_exists");
            return false;
        }
        itemRepo.save(item);
        return true;
    }

    public Item findByStoreAndItemName(GroceryStore store, String itemName) {
        Item item=itemRepo.findByStoreAndName(store, itemName);
        if (item==null){
            Messages.displayErrorMessage("item_identifier_does_not_exist");
        }
        return item;
    }

    public void displayItemsAlphabetic(GroceryStore store) {
        List<Item> items = itemRepo.findByStore(store);
        items.sort(Comparator.comparing(Item::getName));
        items.forEach(Item::display);
    }


}
